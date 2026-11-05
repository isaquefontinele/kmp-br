package br.kmp.documents

/**
 * Receita Federal mod-11 check digit: weighted sum of [values] (left to right) modulo 11;
 * a remainder below 2 gives 0, anything else gives `11 - remainder`.
 */
internal fun mod11CheckDigit(values: IntArray, weights: IntArray): Int {
    require(values.size == weights.size) { "values and weights must have the same size" }
    var sum = 0
    for (i in values.indices) sum += values[i] * weights[i]
    val remainder = sum % 11
    return if (remainder < 2) 0 else 11 - remainder
}

/** Numeric value of a document character as defined by the Receita Federal: its ASCII code minus 48. */
internal fun Char.documentValue(): Int = code - 48

/** Characters removed by `strip`: the mask separators and whitespace. */
internal fun String.removeSeparators(): String =
    filterNot { it == '.' || it == '-' || it == '/' || it.isWhitespace() }

/**
 * A fixed document mask where `#` stands for one document character and every other character is
 * a literal separator, e.g. `###.###.###-##`.
 */
internal class DocumentMask(private val pattern: String) {
    val length: Int = pattern.count { it == '#' }

    /** True when [value] has exactly the mask's separators in the right places. */
    fun matches(value: String): Boolean {
        if (value.length != pattern.length) return false
        for (i in pattern.indices) {
            val p = pattern[i]
            if (p != '#' && value[i] != p) return false
            if (p == '#' && !value[i].isLetterOrDigit()) return false
        }
        return true
    }

    /** Applies the mask to [raw], which must have exactly [length] characters. */
    fun apply(raw: String): String {
        require(raw.length == length) { "Expected $length characters, got ${raw.length}" }
        val out = StringBuilder(pattern.length)
        var next = 0
        for (p in pattern) out.append(if (p == '#') raw[next++] else p)
        return out.toString()
    }
}
