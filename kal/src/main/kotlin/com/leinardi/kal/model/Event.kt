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

sealed class Event {
    data class ButtonPressed(val clientId: String, val button: Button, val motionEvent: MotionEvent) : Event()
    data class CurrentApp(val clientId: String, val app: String) : Event()
    data class DayNightChanged(val isNight: Boolean) : Event()
    data class EnergyProfileChanged(val energySaving: Boolean) : Event()
    data class SettingsAvailable(val clientId: String) : Event()
    data class ShowNotification(val notification: Notification) : Event()
    data class StatsReceived(val clientId: String, val stats: Stats) : Event()
}
