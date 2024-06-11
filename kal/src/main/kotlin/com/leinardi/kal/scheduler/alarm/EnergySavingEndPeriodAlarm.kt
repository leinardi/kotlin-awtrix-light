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

package com.leinardi.kal.scheduler.alarm

import com.leinardi.kal.event.EventHandler
import com.leinardi.kal.interactor.GetEnergySavingPeriodInteractor
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import javax.inject.Inject

class EnergySavingEndPeriodAlarm @Inject constructor(
    private val getEnergySavingPeriodInteractor: GetEnergySavingPeriodInteractor
) : Alarm {
    override val name: String = "EnergySavingEndPeriodAlarm"

    override fun getJobDetail(): JobDetail =
        JobBuilder.newJob(EnergySavingEndPeriodJob::class.java)
            .withIdentity(name)
            .build()

    override fun getTrigger(): Trigger {
        val time = getEnergySavingPeriodInteractor().end
        return TriggerBuilder.newTrigger()
            .withIdentity(name)
            .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(time.hour, time.minute))
            .build()
    }

    class EnergySavingEndPeriodJob @Inject constructor(
        private val eventHandler: EventHandler,
    ) : Job {
        override fun execute(context: JobExecutionContext) {
            logger.debug { "EnergySavingEndPeriodJob: Sending Energy Profile changed" }

            eventHandler.sendEvent(Event.EnergyProfileChanged(false))
        }
    }
}
