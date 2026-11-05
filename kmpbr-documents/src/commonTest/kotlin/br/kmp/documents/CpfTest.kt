package br.kmp.documents

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

// All values are synthetic: check digits computed from arbitrary bases.
class CpfTest {
    private val valid = listOf("12345678909", "98765432100", "00000000191", "39053344705", "47258136991")

    @Test
    fun acceptsValidUnformattedValues() {
        valid.forEach { assertTrue(Cpf.isValid(it), it) }
    }

    @Test
    fun acceptsValidFormattedValues() {
        assertTrue(Cpf.isValid("123.456.789-09"))
        assertTrue(Cpf.isValid("987.654.321-00"))
        assertTrue(Cpf.isValid("000.000.001-91"))
    }

    @Test
    fun ignoresSurroundingWhitespace() {
        assertTrue(Cpf.isValid("  123.456.789-09 "))
        assertTrue(Cpf.isValid("\t12345678909\n"))
    }

    @Test
    fun rejectsWrongCheckDigits() {
        assertFalse(Cpf.isValid("12345678900"))
        assertFalse(Cpf.isValid("12345678919"))
        assertFalse(Cpf.isValid("123.456.789-10"))
    }

    @Test
    fun rejectsRepeatedDigits() {
        // Every one of these passes the mod-11 check, so they must be rejected explicitly.
        for (d in '0'..'9') {
            val raw = d.toString().repeat(11)
            assertFalse(Cpf.isValid(raw), raw)
            assertFalse(Cpf.isValid(Cpf.format(raw)), raw)
        }
        assertFalse(Cpf.isValid("111.111.111-11"))
    }

    @Test
    fun rejectsWrongLength() {
        assertFalse(Cpf.isValid(""))
        assertFalse(Cpf.isValid("   "))
        assertFalse(Cpf.isValid("1234567890"))
        assertFalse(Cpf.isValid("123456789090"))
        assertFalse(Cpf.isValid("123.456.789-0"))
    }

    @Test
    fun rejectsNonDigitCharacters() {
        assertFalse(Cpf.isValid("1234567890a"))
        assertFalse(Cpf.isValid("123.456.78a-09"))
        assertFalse(Cpf.isValid("１２３４５６７８９０９")) // full-width digits
    }

    @Test
    fun rejectsPartialOrUnusualFormatting() {
        assertFalse(Cpf.isValid("123456789-09"))
        assertFalse(Cpf.isValid("123.456.78909"))
        assertFalse(Cpf.isValid("123-456-789.09"))
        assertFalse(Cpf.isValid("123 456 789 09"))
        assertFalse(Cpf.isValid("123/456/789-09"))
    }

    @Test
    fun stripRemovesSeparatorsOnly() {
        assertEquals("12345678909", Cpf.strip("123.456.789-09"))
        assertEquals("12345678909", Cpf.strip(" 123 456 789/09 "))
        assertEquals("12345678909", Cpf.strip("12345678909"))
        assertEquals("123a", Cpf.strip("1.2-3a"))
        assertEquals("", Cpf.strip(""))
    }

    @Test
    fun formatsUnformattedAndFormattedInput() {
        assertEquals("123.456.789-09", Cpf.format("12345678909"))
        assertEquals("123.456.789-09", Cpf.format("123.456.789-09"))
        assertEquals("123.456.789-09", Cpf.format(" 123456789-09 "))
    }

    @Test
    fun formatDoesNotCheckCheckDigits() {
        assertEquals("123.456.789-00", Cpf.format("12345678900"))
    }

    @Test
    fun formatRejectsInputThatIsNotElevenDigits() {
        assertFailsWith<IllegalArgumentException> { Cpf.format("1234567890") }
        assertFailsWith<IllegalArgumentException> { Cpf.format("123456789012") }
        assertFailsWith<IllegalArgumentException> { Cpf.format("1234567890a") }
        assertNull(Cpf.formatOrNull(""))
        assertNull(Cpf.formatOrNull("abc"))
    }

    @Test
    fun generatedValuesAreValidUnformattedAndNotRepeated() {
        val random = Random(42)
        repeat(1_000) {
            val cpf = Cpf.generate(random)
            assertEquals(Cpf.LENGTH, cpf.length)
            assertTrue(cpf.all { c -> c in '0'..'9' }, cpf)
            assertTrue(Cpf.isValid(cpf), cpf)
            assertTrue(Cpf.isValid(Cpf.format(cpf)), cpf)
        }
    }

    @Test
    fun generateIsDeterministicForASeed() {
        assertEquals(Cpf.generate(Random(7)), Cpf.generate(Random(7)))
        assertTrue(Cpf.isValid(Cpf.generate()))
    }

    @Test
    fun changingACheckDigitInvalidates() {
        valid.forEach { cpf ->
            for (i in 9 until Cpf.LENGTH) {
                for (d in '0'..'9') {
                    if (d == cpf[i]) continue
                    val mutated = cpf.substring(0, i) + d + cpf.substring(i + 1)
                    assertFalse(Cpf.isValid(mutated), "$cpf -> $mutated")
                }
            }
        }
    }
}
