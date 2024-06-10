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
import com.leinardi.kal.ext.yearsSince
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.Notification
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import java.time.LocalDate
import java.util.Locale

class BirthdayAlarm(
    override val di: DI,
    private val birthdayData: BirthdayData
) : Alarm, DIAware {

    override val name: String = "BirthdayAlarm"

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

    class BirthdayJob(override val di: DI) : Job, DIAware {
        override fun execute(context: JobExecutionContext) {
            val eventHandler: EventHandler by di.instance()
            val birthdayData = context.mergedJobDataMap["birthdayData"] as BirthdayData

            logger.debug { "BirthdayJob: Sending Birthday Event for $birthdayData" }
            eventHandler.sendEvent(Event.ShowNotification(birthdayData.toNotification()))
        }
    }

    data class BirthdayData(
        val dateOfBirth: LocalDate,
        val name: String,
        val duration: Int? = 60,
        val icon: String? = "14004",  // https://developer.lametric.com/content/apps/icon_thumbs/14004
        val message: String = "Happy ${dateOfBirth.yearsSince()} Birthday $name!",
        val rainbow: Boolean? = true,
        val rtttl: String? = "happybirthday:d=4,o=4,b=120:8d, 8d, e, d",
        val wakeup: Boolean? = true,
    ) {
        fun toNotification() = Notification(
            duration = duration,
            icon = icon,
            loopSound = false,
            rainbow = rainbow,
            rtttl = rtttl,
            text = message,
            wakeup = wakeup,
        )
    }
}

val birthdayList = listOf(
    BirthdayAlarm.BirthdayData(
        dateOfBirth = LocalDate.parse("1981-06-13"),
        name = "Susi",
    ),
    BirthdayAlarm.BirthdayData(
        dateOfBirth = LocalDate.parse("1982-12-22"),
        name = "Roberto",
    ),
)
