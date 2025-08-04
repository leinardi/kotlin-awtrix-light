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

import java.time.LocalTime
import java.time.ZonedDateTime

data class EnergySavingPeriod(val start: LocalTime, val end: LocalTime) {
    fun resolveNextScheduledTime(currentDateTime: ZonedDateTime = ZonedDateTime.now()): ZonedDateTime {
        val startDateTime = currentDateTime.with(start)
        val endDateTime = currentDateTime.with(end).run {
            if (end.isBefore(start)) {
                plusDays(1)
            } else {
                this
            }
        }

        return when {
            currentDateTime.isBefore(startDateTime) -> startDateTime
            !currentDateTime.isBefore(startDateTime) && currentDateTime.isBefore(endDateTime) -> endDateTime
            else -> startDateTime.plusDays(1)
        }
    }

    fun isEnergySavingTime(currentDateTime: ZonedDateTime = ZonedDateTime.now()): Boolean {
        val startDateTime = currentDateTime.with(start)
        val endDateTime = currentDateTime.with(end).run {
            if (end.isBefore(start)) {
                plusDays(1)
            } else {
                this
            }
        }
        return !currentDateTime.isBefore(startDateTime) && currentDateTime.isBefore(endDateTime)
    }
}
