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

package com.leinardi.kal.ui.theme

import com.leinardi.kal.model.Color

object Theme {
    object Day : DayNightTheme {
        override val calendarAccent = Color(0x5A55F7)
        override val contentColor = Color(0xFFFFFF)
    }

    object Night : DayNightTheme {
        override val calendarAccent = Color(0x5A55F7)
        override val contentColor = Color(0x252525)
    }

    private interface DayNightTheme {
        val calendarAccent: Color
        val contentColor: Color
    }
}
