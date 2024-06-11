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

import com.leinardi.kal.ext.yearsSince
import com.leinardi.kal.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Serializable
data class BirthdayData(
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate,
    val name: String,
    val duration: Int? = TimeUnit.MINUTES.toSeconds(10).toInt(),
    val icon: String? = "14004", // https://developer.lametric.com/content/apps/icon_thumbs/14004
    val message: String = "Happy ${dateOfBirth.yearsSince()} Birthday $name!",
    val rainbow: Boolean? = true,
    val rtttl: String? = "happybirthday:d=4,o=4,b=120:8d, 8d, e, d",
    val scrollSpeed: Int? = 50,
    val wakeup: Boolean? = true,
) {
    fun toNotification() = Notification(
        duration = duration,
        icon = icon,
        loopSound = false,
        rainbow = rainbow,
        rtttl = rtttl,
        scrollSpeed = scrollSpeed,
        text = message,
        wakeup = wakeup,
    )
}
