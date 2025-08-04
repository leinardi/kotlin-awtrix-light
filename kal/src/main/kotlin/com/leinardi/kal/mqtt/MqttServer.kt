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

package com.leinardi.kal.mqtt

import com.leinardi.kal.coroutine.CoroutineDispatchers
import com.leinardi.kal.interactor.GetConfigInteractor
import com.leinardi.kal.log.logger
import io.github.davidepianca98.mqtt.broker.Broker
import io.github.davidepianca98.mqtt.packets.Qos
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttServer @Inject constructor(
    auth: MqttAuthenticator,
    getConfigInteractor: GetConfigInteractor,
    packetInterceptor: MqttPacketHandler,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    private val broker by lazy {
        getConfigInteractor().let { config ->
            Broker(
                authentication = auth,
                packetInterceptor = packetInterceptor,
                webSocketPort = config.wsPort,
                port = config.mqttPort,
            )
        }
    }

    fun start() {
        logger.info { "Starting blocking MQTT broker: mqtt=${broker.port} ws=${broker.webSocketPort}" }
        broker.listen()
    }

    fun stop() {
        logger.info { "Stopping blocking MQTT broker: mqtt=${broker.port} ws=${broker.webSocketPort}" }
        broker.stop()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun send(topic: String, payload: String?): Boolean = withContext(coroutineDispatchers.io) {
        broker.publish(
            retain = false,
            topicName = topic,
            qos = Qos.EXACTLY_ONCE,
            properties = null,
            payload = payload?.run { toByteArray().toUByteArray() },
        )
    }

    fun getConnectedClientIds(): Set<String> = broker.getConnectedClientIds()

    fun isClientConnected(clientId: String): Boolean = broker.isClientConnected(clientId)
}
