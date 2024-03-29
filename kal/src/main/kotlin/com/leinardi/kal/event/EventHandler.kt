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
import com.leinardi.kal.interactor.PublishInteractor
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.Publishable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class EventHandler(override val di: DI) : DIAware {
    private val channel = Channel<Event>(UNLIMITED)
    private val clientStateManager: ClientStateManager by di.instance()
    private val coroutineDispatchers: CoroutineDispatchers by di.instance()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)
    private val getSettingsInteractor: GetSettingsInteractor by di.instance()
    private val publishInteractor: PublishInteractor by di.instance()

    init {
        coroutineScope.launch {
            for (event in channel) {
                when (event) {
                    is Event.ButtonPressed -> logger.debug { "Event Received: $event" }
                    is Event.CurrentApp -> clientStateManager.currentApp[event.clientId] = event.app
                    is Event.DayNightChanged -> handleDayNightChanged(event)
                    is Event.DeviceConnected -> clientStateManager.connectedDevices.add(event.clientId)
                    is Event.DeviceDisconnected -> clientStateManager.connectedDevices.remove(event.clientId)
                    is Event.SettingsAvailable -> handleSettingsIsAvailable(event)
                    is Event.StatsReceived -> handleStatsReceived(event)
                }
            }
        }
    }

    private suspend fun handleSettingsIsAvailable(event: Event.SettingsAvailable) {
        logger.debug { "SettingsIsAvailable" }
        refreshSettings(event.clientId)
    }

    private suspend fun handleDayNightChanged(event: Event.DayNightChanged) {
        logger.debug { "DayNightChanged: night = ${event.isNight}" }
        clientStateManager.connectedDevices.forEach { clientId ->
            refreshSettings(clientId)
        }
    }

    private fun handleStatsReceived(event: Event.StatsReceived) {
        clientStateManager.connectedDevices.add(event.clientId)
        clientStateManager.lastReceivedStats[event.clientId] = event.stats
    }

    private suspend fun refreshSettings(clientId: String) {
        val settings = Publishable.Settings(
            clientId = clientId,
            payload = getSettingsInteractor(),
        )
        logger.debug { "Sending settings to the device ${settings.clientId}: ${settings.payload}" }
        publishInteractor(settings)
    }

    fun sendEvent(event: Event) {
        channel.trySend(event)
    }
}
