package com.leinardi.kal.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ColorTest {
    @Test
    fun `The hex value and string representation matches`() {
        val expectedHex = 0xAB1289
        val expectedString = "#AB1289"
        val color = Color(expectedHex)
        val color2 = Color.parseColor(color.toString())
        assertEquals(expectedString, color.toString())
        assertEquals(expectedString, color2.toString())
    }
}
