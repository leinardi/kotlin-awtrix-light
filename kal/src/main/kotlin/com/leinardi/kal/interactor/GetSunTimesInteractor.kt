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

import org.shredzone.commons.suncalc.SunTimes
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSunTimesInteractor @Inject constructor() {
    operator fun invoke(time: ZonedDateTime = ZonedDateTime.now()): SunTimes = SunTimes.compute()
        .on(time)
        .at(MY_LOCATION)
        .elevation(MY_ELEVATION)
        .twilight(SunTimes.Twilight.VISUAL)
        .fullCycle()
        .execute()

    companion object {
        const val MY_ELEVATION: Double = 530.0
        val MY_LOCATION: DoubleArray = doubleArrayOf(48.137154, 11.576124)
    }
}
