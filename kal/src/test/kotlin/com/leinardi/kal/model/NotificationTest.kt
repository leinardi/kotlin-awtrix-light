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

class NotificationTest : DIAware {
    override val di = DI { build() }

    private val json: Json by di.instance()

    @Test
    fun `Test serialization and deserialization`() {
        val notification = Notification(
            text = "Hello, AWTRIX Light!",
            textCase = 2,
            topText = true,
            textOffset = 1,
            center = true,
            color = Color.parseColor("#ff0000"),
            gradient = List(3) { Color.parseColor("#ff0000") },
            blinkText = 2,
            fadeText = 2,
            background = Color.parseColor("#ff0000"),
            rainbow = true,
            icon = "condimentum",
            pushIcon = 1,
            repeat = 2,
            duration = 10,
            hold = true,
            sound = "ius",
            rtttl = "persecuti",
            loopSound = true,
            bar = List(11) { 3 },
            line = List(11) { 3 },
            autoscale = true,
            progress = 100,
            progressC = Color.parseColor("#ff0000"),
            progressBC = Color.parseColor("#ff0000"),
            draw = listOf(
                Draw.Circle(28, 4, 3, Color.parseColor("#FF0000")),
                Draw.FilledCircle(28, 4, 3, Color.parseColor("#FF0000")),
                Draw.Rectangle(20, 4, 4, 4, Color.parseColor("#0000FF")),
                Draw.FilledRectangle(20, 4, 4, 4, Color.parseColor("#0000FF")),
                Draw.Text(0, 0, "Hello", Color.parseColor("#00FF00")),
                Draw.Pixel(0, 0, Color.parseColor("#00FF00")),
            ),
            stack = true,
            wakeup = true,
            noScroll = true,
            clients = List(3) { "clientId" },
            scrollSpeed = 100,
            effect = "semper",
            effectSettings = EffectSettings(speed = 3, palette = "Rainbow", blend = true),
        )
        val jsonString = json.encodeToString(notification)
        assertEquals(notification, json.decodeFromString(jsonString))
    }
}
