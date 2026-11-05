package br.kmp.documents

import kotlin.random.Random

/**
 * CNPJ (Cadastro Nacional da Pessoa Jurídica): the 14-character Brazilian company registration
 * number, written as `12.345.678/0001-95`. The first 8 characters identify the company, the next 4
 * the branch, and the last 2 are mod-11 check digits.
 *
 * Check digits are computed with the Receita Federal rule where each character is worth its ASCII
 * code minus 48, so the same algorithm also covers the alphanumeric CNPJ issued since July 2026.
 * This version accepts **numeric CNPJs only**; alphanumeric support is planned.
 */
public object Cnpj {
    /** Number of characters in a CNPJ, check digits included. */
    public const val LENGTH: Int = 14

    private const val BASE_LENGTH = 12
    private const val BRANCH_HEADQUARTERS = "0001"

    private val mask = DocumentMask("##.###.###/####-##")
    private val firstWeights = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
    private val secondWeights = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

    /**
     * Returns true when [value] is a valid CNPJ, either unformatted (`12345678000195`) or fully
     * formatted (`12.345.678/0001-95`). Leading and trailing whitespace is ignored. Partially
     * formatted input, other separators and sequences of one repeated digit are invalid.
     */
    public fun isValid(value: String): Boolean {
        val trimmed = value.trim()
        val raw = when {
            trimmed.length == LENGTH -> trimmed
            mask.matches(trimmed) -> trimmed.removeSeparators()
            else -> return false
        }
        if (!hasValidCharacters(raw)) return false
        if (raw.all { it == raw[0] }) return false
        val base = raw.substring(0, BASE_LENGTH)
        return base + checkDigits(base) == raw
    }

    /** Removes the separators `.`, `-`, `/` and whitespace from [value]. Other characters are kept. */
    public fun strip(value: String): String = value.removeSeparators()

    /**
     * Formats [value] as `##.###.###/####-##`. [value] may be formatted or not, but must contain
     * exactly 14 digits once [stripped][strip]. Check digits are not verified; use [isValid] for that.
     *
     * @throws IllegalArgumentException if [value] does not contain exactly 14 digits.
     */
    public fun format(value: String): String =
        formatOrNull(value) ?: throw IllegalArgumentException("A CNPJ must have exactly $LENGTH digits")

    /** Like [format], but returns null instead of throwing when [value] cannot be formatted. */
    public fun formatOrNull(value: String): String? {
        val raw = strip(value)
        if (raw.length != LENGTH || !hasValidCharacters(raw)) return null
        return mask.apply(raw)
    }

    /**
     * Generates a random, unformatted numeric CNPJ for a headquarters (branch `0001`) with valid
     * check digits, **for tests and examples only**. It is not checked against any registry and may
     * belong to a real company by coincidence.
     */
    public fun generate(): String = generate(Random.Default)

    /** Like [generate], using [random] as the source of randomness (e.g. a seeded one in tests). */
    public fun generate(random: Random): String {
        val root = CharArray(8) { '0' + random.nextInt(10) }.concatToString()
        val base = root + BRANCH_HEADQUARTERS
        return base + checkDigits(base)
    }

    private fun hasValidCharacters(raw: String): Boolean {
        for (i in raw.indices) {
            if (!(if (i < BASE_LENGTH) isBaseChar(raw[i]) else raw[i] in '0'..'9')) return false
        }
        return true
    }

    // Numeric only for now; the alphanumeric CNPJ widens this to 0-9 and A-Z.
    private fun isBaseChar(c: Char): Boolean = c in '0'..'9'

    private fun checkDigits(base: String): String {
        val values = IntArray(base.length) { base[it].documentValue() }
        val first = mod11CheckDigit(values, firstWeights)
        val second = mod11CheckDigit(values + first, secondWeights)
        return "$first$second"
    }
}
