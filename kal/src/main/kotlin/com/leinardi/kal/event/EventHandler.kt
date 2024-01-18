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
import com.leinardi.kal.interactor.GetSettingsInteractor
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.Publishable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch

class EventHandler(
    coroutineDispatchers: CoroutineDispatchers,
    private val clientStateManager: ClientStateManager,
    private val getSettingsInteractor: GetSettingsInteractor,
    private val publishableChannel: Channel<Publishable>
) {
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)
    private val channel = Channel<Event>(UNLIMITED)

    init {
        coroutineScope.launch {
            for (event in channel) {
                when (event) {
                    is Event.ButtonPressed -> logger.debug { "Event Received: $event" }
                    is Event.SettingsAvailable -> handleSettingsIsAvailable(event)
                    is Event.DayNightChanged -> logger.debug { "DayNight: night = ${event.isNight}" }
                    is Event.CurrentApp -> clientStateManager.currentApp[event.clientId] = event.app
                    is Event.StatsReceived -> {
                        clientStateManager.lastReceivedStats[event.clientId] = event.stats
                        logger.debug { "Battery = ${event.stats.bat} (raw=${event.stats.batRaw})" }
                    }
                }
            }
        }
    }

    private fun handleSettingsIsAvailable(event: Event.SettingsAvailable) {
        publishableChannel.trySend(
            Publishable.Settings(
                clientId = event.clientId,
                payload = getSettingsInteractor(),
            ),
        )
    }

    fun sendEvent(event: Event) {
        channel.trySend(event)
    }
}
