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

package com.leinardi.kal.ext

import org.quartz.Trigger
import org.quartz.impl.triggers.CronTriggerImpl
import org.quartz.impl.triggers.SimpleTriggerImpl

fun Trigger.getLogMessage(): String {
    val sb = StringBuilder()
    sb.append("Trigger Type: ${this::class.java.simpleName}\n")
    sb.append("Key: ${this.key}\n")
    sb.append("Description: ${this.description}\n")
    sb.append("Priority: ${this.priority}\n")
    sb.append("Job Key: ${this.jobKey}\n")
    sb.append("StartTime: ${this.startTime}\n")
    sb.append("EndTime: ${this.endTime}\n")
    sb.append("Calendar Name: ${this.calendarName}\n")
    val misfireInstruction = when (this.misfireInstruction) {
        -1 -> "MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY"
        0 -> "MISFIRE_INSTRUCTION_SMART_POLICY"
        1 -> "MISFIRE_INSTRUCTION_FIRE_NOW"
        2 -> "MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT"
        3 -> "MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT"
        4 -> "MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT"
        5 -> "MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT"
        else -> this.misfireInstruction.toString()
    }
    sb.append("Misfire Instruction: ${misfireInstruction}\n")

    when (this) {
        is SimpleTriggerImpl -> {
            sb.append("Repeat Interval: ${this.repeatInterval} milliseconds\n")
            sb.append("Repeat Count: ${this.repeatCount}\n")
        }

        is CronTriggerImpl -> {
            sb.append("Cron Expression: ${this.cronExpression}\n")
        }
    }

    return sb.toString()
}
