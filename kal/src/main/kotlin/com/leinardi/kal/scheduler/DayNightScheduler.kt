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

package com.leinardi.kal.scheduler

import com.leinardi.kal.event.EventHandler
import com.leinardi.kal.interactor.GetSunTimesInteractor
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.time.Duration
import java.time.ZonedDateTime

class DayNightScheduler(override val di: DI) : DIAware {
    private val eventHandler: EventHandler by di.instance()
    private val getSunTimesInteractor: GetSunTimesInteractor by di.instance()
    private val job = SupervisorJob()

    suspend fun start(coroutineScope: CoroutineScope) {
        // Schedule updates
        while (coroutineScope.isActive) {
            val scheduledTime = getScheduledTime()
            val timeUntilNextDayNightChange = Duration.between(ZonedDateTime.now(), scheduledTime.time)
            logger.debug { "Time until next DayNight change = $timeUntilNextDayNightChange" }
            delay(timeUntilNextDayNightChange.toMillis())
            sendDayNightChanged(scheduledTime.isNight)
        }
    }

    fun stopScheduler() {
        job.cancel()
    }

    private fun sendDayNightChanged(isNight: Boolean) {
        logger.debug { "Sending DayNight changed" }
        eventHandler.sendEvent(Event.DayNightChanged(isNight))
    }

    private fun getScheduledTime(): ScheduledTime {
        val currentTime = ZonedDateTime.now()
        val times = getSunTimesInteractor(currentTime)

        val sunriseTime = checkNotNull(times.rise)
        val sunsetTime = checkNotNull(times.set)

        logger.debug { "sunrise = $sunriseTime sunset = $sunsetTime" }

        val scheduledTime: ZonedDateTime = if (sunriseTime.isBefore(sunsetTime)) sunriseTime else sunsetTime
        val isNight: Boolean = scheduledTime == sunsetTime

        return ScheduledTime(scheduledTime, isNight)
    }

    private data class ScheduledTime(val time: ZonedDateTime, val isNight: Boolean)

    companion object {
        val MY_LOCATION: DoubleArray = doubleArrayOf(48.137154, 11.576124)
    }
}
