/*
 * Copyright 2024 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
