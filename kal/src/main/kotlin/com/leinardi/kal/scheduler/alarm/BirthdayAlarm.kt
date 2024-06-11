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
import com.leinardi.kal.model.BirthdayData
import com.leinardi.kal.model.Event
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import java.util.Locale
import javax.inject.Inject

class BirthdayAlarm @AssistedInject constructor(
    @Assisted private val birthdayData: BirthdayData,
) : Alarm {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted birthdayData: BirthdayData,
        ): BirthdayAlarm
    }

    override val name: String = "BirthdayAlarm${birthdayData.name}-${birthdayData.dateOfBirth}"

    override fun getJobDetail(): JobDetail {
        val jobDataMap = JobDataMap().apply { put("birthdayData", birthdayData) }
        return JobBuilder.newJob(BirthdayJob::class.java)
            .withIdentity(name)
            .setJobData(jobDataMap)
            .build()
    }

    override fun getTrigger(): Trigger {
        val cronExpression = String.format(Locale.ROOT, "0 0 0 %d %d ? *", birthdayData.dateOfBirth.dayOfMonth, birthdayData.dateOfBirth.monthValue)
        return TriggerBuilder.newTrigger()
            .withIdentity(name)
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build()
    }

    class BirthdayJob @Inject constructor(
        private val eventHandler: EventHandler,
    ) : Job {
        override fun execute(context: JobExecutionContext) {
            val birthdayData = context.mergedJobDataMap["birthdayData"] as BirthdayData

            logger.debug { "BirthdayJob: Sending Birthday Event for $birthdayData" }
            eventHandler.sendEvent(Event.ShowNotification(birthdayData.toNotification()))
        }
    }
}
