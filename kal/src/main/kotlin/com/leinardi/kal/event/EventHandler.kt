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

package com.leinardi.kal.event

import com.leinardi.kal.awtrix.ClientStateManager
import com.leinardi.kal.coroutine.CoroutineDispatchers
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Button
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.MotionEvent
import com.leinardi.kal.model.Publishable
import com.leinardi.kal.model.Settings
import com.leinardi.kal.model.Sleep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch

class EventHandler(
    coroutineDispatchers: CoroutineDispatchers,
    private val clientStateManager: ClientStateManager,
    private val publishableChannel: Channel<Publishable>
) {
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)
    private val channel = Channel<Event>(UNLIMITED)

    init {
        coroutineScope.launch {
            for (event in channel) {
                when (event) {
                    is Event.ButtonPressed -> {
                        logger.debug { "Event Received: $event" }
                        if (event.button == Button.SELECT && event.motionEvent == MotionEvent.ACTION_UP) {
                            publishableChannel.trySend(
                                Publishable.Sleep(event.clientId, Sleep(5)),
                            )
                        }
                    }

                    is Event.SettingsAvailable -> publishableChannel.trySend(
                        Publishable.Settings(
                            clientId = event.clientId,
                            payload = Settings(
                                calendarHeaderColor = "#3E6BD1",
                                calendarBackgroundColor = "#8B8B8B",
                                weekdayActiveColor = "#3E6BD1",
                                weekdayInactiveColor = "#8B8B8B",
                            ),
                        ),
                    )

                    is Event.CurrentApp -> clientStateManager.currentApp[event.clientId] = event.app
                    is Event.StatsReceived -> clientStateManager.lastReceivedStats[event.clientId] = event.stats
                }
            }
        }
    }

    fun sendEvent(event: Event) {
        channel.trySend(event)
    }
}
