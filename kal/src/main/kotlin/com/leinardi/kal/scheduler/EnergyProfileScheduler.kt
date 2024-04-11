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
import com.leinardi.kal.interactor.GetEnergySavingPeriodInteractor
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

class EnergyProfileScheduler(override val di: DI) : DIAware {
    private val eventHandler: EventHandler by di.instance()
    private val getEnergySavingPeriodInteractor: GetEnergySavingPeriodInteractor by di.instance()
    private val job = SupervisorJob()

    suspend fun start(coroutineScope: CoroutineScope) {
        // Schedule updates
        while (coroutineScope.isActive) {
            val scheduledTime = getScheduledTime()
            val timeUntilNextEnergyProfileChange = Duration.between(ZonedDateTime.now(), scheduledTime.time)
            logger.debug { "Time until next Energy Profile change = $timeUntilNextEnergyProfileChange" }
            delay(timeUntilNextEnergyProfileChange.toMillis())
            sendEnergyProfileChanged(scheduledTime.energySaving)
        }
    }

    fun stopScheduler() {
        job.cancel()
    }

    private fun sendEnergyProfileChanged(energySaving: Boolean) {
        logger.debug { "Sending Energy Profile changed" }
        eventHandler.sendEvent(Event.EnergyProfileChanged(energySaving))
    }

    private fun getScheduledTime(): ScheduledTime {
        val energySavingPeriod = getEnergySavingPeriodInteractor()
        val scheduledTime = energySavingPeriod.resolveNextScheduledTime()
        logger.debug { "next energy saving change time = $scheduledTime" }
        return ScheduledTime(scheduledTime, false)
    }

    private data class ScheduledTime(val time: ZonedDateTime, val energySaving: Boolean)
}
