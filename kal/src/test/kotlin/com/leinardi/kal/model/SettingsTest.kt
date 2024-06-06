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

import com.leinardi.kal.di.build
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsTest : DIAware {
    override val di = DI { build() }

    private val json: Json by di.instance()

    @Test
    fun `Test serialization and deserialization`() {
        val settings = Settings(
            appTime = 2282,
            transitionEffect = Settings.TransitionEffect.RIPPLE,
            transitionSpeed = 5293,
            transitionColor = Color.parseColor("#ff0000"),
            timeMode = Settings.TimeMode.MODE2,
            calendarHeaderColor = Color.parseColor("#ff0000"),
            calendarBackgroundColor = Color.parseColor("#ff0000"),
            calendarTextColor = Color.parseColor("#ff0000"),
            weekdayDisplay = true,
            weekdayActiveColor = Color.parseColor("#ff0000"),
            weekdayInactiveColor = Color.parseColor("#ff0000"),
            brightness = 9985,
            autoBrightness = true,
            autoTransition = true,
            colorCorrection = listOf(255, 0, 0),
            colorTemperature = listOf(255, 0, 0),
            timeFormat = Settings.TimeFormat.FORMAT7,
            dateFormat = Settings.DateFormat.FORMAT4,
            startOfWeekOnMonday = true,
            blocKeysNavigation = true,
            uppercase = true,
            timeColor = Color.parseColor("#ff0000"),
            dateColor = Color.parseColor("#ff0000"),
            temperatureColor = Color.parseColor("#ff0000"),
            humidityColor = Color.parseColor("#ff0000"),
            batteryColor = Color.parseColor("#ff0000"),
            scrollSpeed = 4422,
            timeApp = true,
            dateApp = true,
            humidityApp = true,
            temperatureApp = true,
            batteryApp = true,
            matrixEnabled = true,
            volumeDfplayer = 9985,
            overlay = OverlayEffect.Rain,
        )
        val jsonString = json.encodeToString(settings)
        assertEquals(settings, json.decodeFromString(jsonString))
    }
}
