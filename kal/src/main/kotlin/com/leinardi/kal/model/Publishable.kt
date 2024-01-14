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

package com.leinardi.kal.model

sealed class Publishable {
    abstract val clientId: String
    abstract val topic: String
    open val payload: Any? = null

    data class Notify(override val clientId: String, override val payload: Notification) : Publishable() {
        override val topic = "$clientId/notify"
    }

    data class Brightness(override val clientId: String) : Publishable() {
        override val topic = "$clientId/brightness"
    }

    data class NotifyDismiss(override val clientId: String) : Publishable() {
        override val topic = "$clientId/notify/dismiss"
    }

    data class CustomApp(override val clientId: String, val appName: String, override val payload: com.leinardi.kal.model.CustomApp) : Publishable() {
        override val topic = "$clientId/custom/$appName"
    }

    data class Switch(override val clientId: String) : Publishable() {
        override val topic = "$clientId/switch"
    }

    data class Settings(override val clientId: String, override val payload: com.leinardi.kal.model.Settings) : Publishable() {
        override val topic = "$clientId/settings"
    }

    data class PreviousApp(override val clientId: String) : Publishable() {
        override val topic = "$clientId/previousapp"
    }

    data class NextApp(override val clientId: String) : Publishable() {
        override val topic = "$clientId/nextapp"
    }

    data class DoupDate(override val clientId: String) : Publishable() {
        override val topic = "$clientId/doupdate"
    }

    data class Apps(override val clientId: String) : Publishable() {
        override val topic = "$clientId/apps"
    }

    data class Power(override val clientId: String, override val payload: com.leinardi.kal.model.Power) : Publishable() {
        override val topic = "$clientId/power"
    }

    data class Sleep(override val clientId: String, override val payload: com.leinardi.kal.model.Sleep) : Publishable() {
        override val topic = "$clientId/sleep"
    }

    data class Indicator1(override val clientId: String, override val payload: Indicator?) : Publishable() {
        override val topic = "$clientId/indicator1"
    }

    data class Indicator2(override val clientId: String, override val payload: Indicator?) : Publishable() {
        override val topic = "$clientId/indicator2"
    }

    data class Indicator3(override val clientId: String, override val payload: Indicator?) : Publishable() {
        override val topic = "$clientId/indicator3"
    }

    data class TimeFormat(override val clientId: String) : Publishable() {
        override val topic = "$clientId/timeformat"
    }

    data class DateFormat(override val clientId: String) : Publishable() {
        override val topic = "$clientId/dateformat"
    }

    data class Reboot(override val clientId: String) : Publishable() {
        override val topic = "$clientId/reboot"
    }

    data class MoodLight(override val clientId: String, override val payload: com.leinardi.kal.model.MoodLight?) : Publishable() {
        override val topic = "$clientId/moodlight"
    }

    data class Sound(override val clientId: String) : Publishable() {
        override val topic = "$clientId/sound"
    }

    data class Rtttl(override val clientId: String, override val payload: com.leinardi.kal.model.Rtttl) : Publishable() {
        override val topic = "$clientId/rtttl"
    }

    data class SendScreen(override val clientId: String) : Publishable() {
        override val topic = "$clientId/sendscreen"
    }

    data class R2d2(override val clientId: String) : Publishable() {
        override val topic = "$clientId/r2d2"
    }
}
