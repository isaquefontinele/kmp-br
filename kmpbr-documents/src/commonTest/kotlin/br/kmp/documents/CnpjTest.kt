package br.kmp.documents

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

// All values are synthetic: check digits computed from arbitrary bases.
class CnpjTest {
    private val valid = listOf(
        "12345678000195",
        "11222333000181",
        "00000000000191",
        "47581369000107",
        "30692714000250",
        "81036452999904",
    )

    @Test
    fun acceptsValidUnformattedValues() {
        valid.forEach { assertTrue(Cnpj.isValid(it), it) }
    }

    @Test
    fun acceptsValidFormattedValues() {
        assertTrue(Cnpj.isValid("12.345.678/0001-95"))
        assertTrue(Cnpj.isValid("11.222.333/0001-81"))
        assertTrue(Cnpj.isValid("30.692.714/0002-50"))
    }

    @Test
    fun ignoresSurroundingWhitespace() {
        assertTrue(Cnpj.isValid("  12.345.678/0001-95 "))
        assertTrue(Cnpj.isValid("\t12345678000195\n"))
    }

    @Test
    fun rejectsWrongCheckDigits() {
        assertFalse(Cnpj.isValid("12345678000194"))
        assertFalse(Cnpj.isValid("12345678000185"))
        assertFalse(Cnpj.isValid("12.345.678/0001-00"))
    }

    @Test
    fun rejectsRepeatedDigits() {
        for (d in '0'..'9') {
            val raw = d.toString().repeat(14)
            assertFalse(Cnpj.isValid(raw), raw)
            assertFalse(Cnpj.isValid(Cnpj.format(raw)), raw)
        }
        assertFalse(Cnpj.isValid("11.111.111/1111-11"))
    }

    @Test
    fun rejectsWrongLength() {
        assertFalse(Cnpj.isValid(""))
        assertFalse(Cnpj.isValid("   "))
        assertFalse(Cnpj.isValid("1234567800019"))
        assertFalse(Cnpj.isValid("123456780001950"))
        assertFalse(Cnpj.isValid("12.345.678/0001-9"))
        assertFalse(Cnpj.isValid("12345678909")) // a CPF
    }

    @Test
    fun rejectsNonDigitCharacters() {
        assertFalse(Cnpj.isValid("1234567800019a"))
        assertFalse(Cnpj.isValid("12.345.678/000a-95"))
        assertFalse(Cnpj.isValid("１２３４５６７８０００１９５")) // full-width digits
    }

    @Test
    fun rejectsAlphanumericCnpjForNow() {
        // Valid alphanumeric CNPJ from the README; accepted once alphanumeric support lands.
        assertFalse(Cnpj.isValid("12ABC34501DE35"))
        assertFalse(Cnpj.isValid("12.ABC.345/01DE-35"))
        assertNull(Cnpj.formatOrNull("12ABC34501DE35"))
    }

    @Test
    fun rejectsPartialOrUnusualFormatting() {
        assertFalse(Cnpj.isValid("12345678/0001-95"))
        assertFalse(Cnpj.isValid("12.345.678000195"))
        assertFalse(Cnpj.isValid("12.345.678-0001/95"))
        assertFalse(Cnpj.isValid("12 345 678 0001 95"))
        assertFalse(Cnpj.isValid("12.345.678.0001-95"))
    }

    @Test
    fun stripRemovesSeparatorsOnly() {
        assertEquals("12345678000195", Cnpj.strip("12.345.678/0001-95"))
        assertEquals("12345678000195", Cnpj.strip(" 12 345 678 0001 95 "))
        assertEquals("12345678000195", Cnpj.strip("12345678000195"))
        assertEquals("12ABC34501DE35", Cnpj.strip("12.ABC.345/01DE-35"))
        assertEquals("", Cnpj.strip(""))
    }

    @Test
    fun formatsUnformattedAndFormattedInput() {
        assertEquals("12.345.678/0001-95", Cnpj.format("12345678000195"))
        assertEquals("12.345.678/0001-95", Cnpj.format("12.345.678/0001-95"))
        assertEquals("12.345.678/0001-95", Cnpj.format(" 12345678/0001-95 "))
    }

    @Test
    fun formatDoesNotCheckCheckDigits() {
        assertEquals("12.345.678/0001-00", Cnpj.format("12345678000100"))
    }

    @Test
    fun formatRejectsInputThatIsNotFourteenDigits() {
        assertFailsWith<IllegalArgumentException> { Cnpj.format("1234567800019") }
        assertFailsWith<IllegalArgumentException> { Cnpj.format("123456780001950") }
        assertFailsWith<IllegalArgumentException> { Cnpj.format("1234567800019a") }
        assertNull(Cnpj.formatOrNull(""))
        assertNull(Cnpj.formatOrNull("abc"))
    }

    @Test
    fun generatedValuesAreValidNumericHeadquarters() {
        val random = Random(42)
        repeat(1_000) {
            val cnpj = Cnpj.generate(random)
            assertEquals(Cnpj.LENGTH, cnpj.length)
            assertTrue(cnpj.all { c -> c in '0'..'9' }, cnpj)
            assertEquals("0001", cnpj.substring(8, 12), cnpj)
            assertTrue(Cnpj.isValid(cnpj), cnpj)
            assertTrue(Cnpj.isValid(Cnpj.format(cnpj)), cnpj)
        }
    }

    @Test
    fun generateIsDeterministicForASeed() {
        assertEquals(Cnpj.generate(Random(7)), Cnpj.generate(Random(7)))
        assertTrue(Cnpj.isValid(Cnpj.generate()))
    }

    @Test
    fun changingACheckDigitInvalidates() {
        valid.forEach { cnpj ->
            for (i in 12 until Cnpj.LENGTH) {
                for (d in '0'..'9') {
                    if (d == cnpj[i]) continue
                    val mutated = cnpj.substring(0, i) + d + cnpj.substring(i + 1)
                    assertFalse(Cnpj.isValid(mutated), "$cnpj -> $mutated")
                }
            }
        }
    }
}
