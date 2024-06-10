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
import com.leinardi.kal.interactor.GetSunTimesInteractor
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import startAt

class SunsetAlarm(override val di: DI) : Alarm, DIAware {
    private val getSunTimesInteractor: GetSunTimesInteractor by di.instance()

    override val name: String = "SunsetAlarm"

    override fun getJobDetail(): JobDetail =
        JobBuilder.newJob(SunsetJob::class.java)
            .withIdentity(name)
            .build()

    override fun getTrigger(): Trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(name)
            .startAt(checkNotNull(getSunTimesInteractor().set))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()

    class SunsetJob(override val di: DI) : Job, DIAware {
        override fun execute(context: JobExecutionContext) {
            logger.warn { "SunsetJob: Sending DayNight changed" }
            val eventHandler: EventHandler by di.instance()
            val getSunTimesInteractor: GetSunTimesInteractor by di.instance()

            eventHandler.sendEvent(Event.DayNightChanged(true))

            context.scheduler.rescheduleJob(
                context.trigger.key,
                context.trigger.triggerBuilder.startAt(checkNotNull(getSunTimesInteractor().set)).build(),
            )
        }
    }
}
