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

package com.leinardi.kal.interactor

import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsNightInteractor @Inject constructor(
    private val getSunTimesInteractor: GetSunTimesInteractor,
) {
    private var cachedResult: Pair<Boolean, ZonedDateTime>? = null
    operator fun invoke(): Boolean {
        val currentTime = ZonedDateTime.now()

        // Check if a cached result exists and is still valid
        cachedResult?.let { (cachedValue, cachedTimestamp) ->
            if (Duration.between(cachedTimestamp, currentTime).toMinutes() < 1) {
                return cachedValue
            }
        }

        // Calculate and cache the new result
        val times = getSunTimesInteractor(ZonedDateTime.now())
        val sunriseTime = checkNotNull(times.rise)
        val sunsetTime = checkNotNull(times.set)
        val result = sunriseTime.isBefore(sunsetTime)

        cachedResult = result to currentTime
        return result
    }
}
