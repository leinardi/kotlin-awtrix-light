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

package com.leinardi.kal.di

import com.leinardi.kal.awtrix.ClientStateManager
import com.leinardi.kal.coroutine.CoroutineDispatchers
import com.leinardi.kal.event.EventHandler
import com.leinardi.kal.model.Publishable
import com.leinardi.kal.mqtt.MqttAuthenticator
import com.leinardi.kal.mqtt.MqttPacketHandler
import com.leinardi.kal.mqtt.MqttServer
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

@OptIn(ExperimentalSerializationApi::class)
fun DI.MainBuilder.build() {
    bindSingleton<Json> {
        Json {
            encodeDefaults = true
            explicitNulls = false
            ignoreUnknownKeys = true
        }
    }
    bindProvider { CoroutineDispatchers() }
    bindSingleton { Channel<Publishable>(Channel.UNLIMITED) }
    bindSingleton { ClientStateManager() }
    bindSingleton { EventHandler(instance(), instance(), instance()) }
    bindSingleton { MqttAuthenticator() }
    bindSingleton { MqttPacketHandler(instance(), instance()) }
    bindSingleton { MqttServer(di, instance(), instance(), instance(), instance(), instance()) }
}