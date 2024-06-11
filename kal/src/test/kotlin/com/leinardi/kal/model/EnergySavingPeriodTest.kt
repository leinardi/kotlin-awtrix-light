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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnergySavingPeriodTest {
    @Test
    fun `Test current time is before start and end is after start`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(4, 0), LocalTime.of(12, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start.minusHours(1))

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is between start and end and end is after start`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(4, 0), LocalTime.of(12, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start.plusSeconds(1))

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.end)
        assertEquals(expectedTime, nextScheduledTime)
        assertTrue(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is after end and end is after start`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(4, 0), LocalTime.of(12, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.end.plusSeconds(1))

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.plusDays(1).with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is exactly at start and end is after start`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(10, 0), LocalTime.of(12, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start)

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.end)
        assertEquals(expectedTime, nextScheduledTime)
        assertTrue(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is exactly at end and end is after start`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(10, 0), LocalTime.of(12, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.end)

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.plusDays(1).with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is before start and end is on the next day`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(23, 0), LocalTime.of(6, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start.minusHours(1))

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is between start and end and end is on the next day`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(23, 0), LocalTime.of(6, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start.plusSeconds(1))

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.plusDays(1).with(timeRange.end)
        assertEquals(expectedTime, nextScheduledTime)
        assertTrue(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is after end and end is on the next day`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(23, 0), LocalTime.of(6, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.end.plusSeconds(1)).plusDays(1)

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is exactly at start and end is on the next day`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(23, 0), LocalTime.of(6, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.start)

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.plusDays(1).with(timeRange.end)
        assertEquals(expectedTime, nextScheduledTime)
        assertTrue(timeRange.isEnergySavingTime(currentTime))
    }

    @Test
    fun `Test current time is exactly at end and end is on the next day`() {
        val timeRange = EnergySavingPeriod(LocalTime.of(23, 0), LocalTime.of(6, 0))
        val currentTime = ZonedDateTime.now().with(timeRange.end)

        val nextScheduledTime = timeRange.resolveNextScheduledTime(currentTime)

        val expectedTime = currentTime.with(timeRange.start)
        assertEquals(expectedTime, nextScheduledTime)
        assertFalse(timeRange.isEnergySavingTime(currentTime))
    }
}
