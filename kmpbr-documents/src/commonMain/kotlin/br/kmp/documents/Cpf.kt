package br.kmp.documents

import kotlin.random.Random

/**
 * CPF (Cadastro de Pessoas Físicas): the 11-digit Brazilian individual taxpayer number, written as
 * `123.456.789-09`. The last two digits are mod-11 check digits.
 */
public object Cpf {
    /** Number of digits in a CPF. */
    public const val LENGTH: Int = 11

    private val mask = DocumentMask("###.###.###-##")
    private val firstWeights = intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2)
    private val secondWeights = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)

    /**
     * Returns true when [value] is a valid CPF, either unformatted (`12345678909`) or fully
     * formatted (`123.456.789-09`). Leading and trailing whitespace is ignored. Partially formatted
     * input, other separators and sequences of one repeated digit (`111.111.111-11`) are invalid.
     */
    public fun isValid(value: String): Boolean {
        val trimmed = value.trim()
        val digits = when {
            trimmed.length == LENGTH -> trimmed
            mask.matches(trimmed) -> trimmed.removeSeparators()
            else -> return false
        }
        if (!digits.all { it in '0'..'9' }) return false
        if (digits.all { it == digits[0] }) return false
        return digits.substring(0, 9) + checkDigits(digits.substring(0, 9)) == digits
    }

    /** Removes the separators `.`, `-`, `/` and whitespace from [value]. Other characters are kept. */
    public fun strip(value: String): String = value.removeSeparators()

    /**
     * Formats [value] as `###.###.###-##`. [value] may be formatted or not, but must contain exactly
     * 11 digits once [stripped][strip]. Check digits are not verified; use [isValid] for that.
     *
     * @throws IllegalArgumentException if [value] does not contain exactly 11 digits.
     */
    public fun format(value: String): String =
        formatOrNull(value) ?: throw IllegalArgumentException("A CPF must have exactly $LENGTH digits")

    /** Like [format], but returns null instead of throwing when [value] cannot be formatted. */
    public fun formatOrNull(value: String): String? {
        val digits = strip(value)
        if (digits.length != LENGTH || !digits.all { it in '0'..'9' }) return null
        return mask.apply(digits)
    }

    /**
     * Generates a random, unformatted CPF with valid check digits, **for tests and examples only**.
     * It is not checked against any registry and may belong to a real person by coincidence.
     */
    public fun generate(): String = generate(Random.Default)

    /** Like [generate], using [random] as the source of randomness (e.g. a seeded one in tests). */
    public fun generate(random: Random): String {
        var base: String
        do {
            base = CharArray(9) { '0' + random.nextInt(10) }.concatToString()
        } while (base.all { it == base[0] })
        return base + checkDigits(base)
    }

    private fun checkDigits(base: String): String {
        val values = IntArray(base.length) { base[it].documentValue() }
        val first = mod11CheckDigit(values, firstWeights)
        val second = mod11CheckDigit(values + first, secondWeights)
        return "$first$second"
    }
}
