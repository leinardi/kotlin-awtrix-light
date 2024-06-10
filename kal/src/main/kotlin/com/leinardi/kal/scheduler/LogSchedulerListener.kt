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

import com.leinardi.kal.ext.getLogMessage
import com.leinardi.kal.log.logger
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.SchedulerException
import org.quartz.SchedulerListener
import org.quartz.Trigger
import org.quartz.TriggerKey

class LogSchedulerListener : SchedulerListener {
    override fun jobScheduled(trigger: Trigger?) {
        logger.debug { "Job scheduled:\n${trigger?.getLogMessage()}" }
    }

    override fun jobUnscheduled(triggerKey: TriggerKey?) {
        logger.debug { "Job Unscheduled: $triggerKey" }
    }

    override fun triggerFinalized(trigger: Trigger?) {
        // No-op
    }

    override fun triggerPaused(triggerKey: TriggerKey?) {
        // No-op
    }

    override fun triggersPaused(triggerGroup: String?) {
        // No-op
    }

    override fun triggerResumed(triggerKey: TriggerKey?) {
        // No-op
    }

    override fun triggersResumed(triggerGroup: String?) {
        // No-op
    }

    override fun jobAdded(jobDetail: JobDetail?) {
        // No-op
    }

    override fun jobDeleted(jobKey: JobKey?) {
        // No-op
    }

    override fun jobPaused(jobKey: JobKey?) {
        // No-op
    }

    override fun jobsPaused(jobGroup: String?) {
        // No-op
    }

    override fun jobResumed(jobKey: JobKey?) {
        // No-op
    }

    override fun jobsResumed(jobGroup: String?) {
        // No-op
    }

    override fun schedulerError(msg: String?, cause: SchedulerException?) {
        msg?.let { logger.error { it } }
        cause?.let { logger.error { it } }
    }

    override fun schedulerInStandbyMode() {
        // No-op
    }

    override fun schedulerStarted() {
        // No-op
    }

    override fun schedulerStarting() {
        // No-op
    }

    override fun schedulerShutdown() {
        // No-op
    }

    override fun schedulerShuttingdown() {
        // No-op
    }

    override fun schedulingDataCleared() {
        // No-op
    }
}
