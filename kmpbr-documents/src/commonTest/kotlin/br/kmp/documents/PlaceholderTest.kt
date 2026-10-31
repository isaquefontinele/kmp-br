package br.kmp.documents

import kotlin.test.Test
import kotlin.test.assertEquals

class PlaceholderTest {
    @Test
    fun moduleNameIsAvailableOnEveryTarget() {
        assertEquals("kmpbr-documents", Placeholder.MODULE)
    }
}
