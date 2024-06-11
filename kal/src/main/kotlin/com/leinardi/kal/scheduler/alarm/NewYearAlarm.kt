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
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.Notification
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewYearAlarm @Inject constructor() : Alarm {
    override val name: String = "NewYearAlarm"

    override fun getJobDetail(): JobDetail =
        JobBuilder.newJob(NewYearJob::class.java)
            .withIdentity(name)
            .build()

    override fun getTrigger(): Trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(name)
            .withSchedule(CronScheduleBuilder.cronSchedule(CRON_AT_THE_BEGINNING_OF_EVERY_YEAR))
            .build()

    class NewYearJob @Inject constructor(
        private val eventHandler: EventHandler,
    ) : Job {
        override fun execute(context: JobExecutionContext) {
            logger.debug { "NewYearJob: sending Happy new year notification!" }

            eventHandler.sendEvent(
                Event.ShowNotification(
                    Notification(
                        text = "Happy New Year ${LocalDate.now().year}!",
                        duration = TimeUnit.MINUTES.toSeconds(10).toInt(),
                        icon = "5855",
                        scrollSpeed = 50,
                        rainbow = true,
                        wakeup = true,
                    ),
                ),
            )
        }
    }

    companion object {
        private const val CRON_AT_THE_BEGINNING_OF_EVERY_YEAR = "0 0 0 1 1 ? *"
    }
}
