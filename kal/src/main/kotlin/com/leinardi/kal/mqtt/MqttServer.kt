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
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Publishable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import mqtt.broker.Broker
import mqtt.packets.Qos
import org.kodein.di.DI
import org.kodein.di.DIAware

class MqttServer(
    override val di: DI,
    auth: MqttAuthenticator,
    packetInterceptor: MqttPacketHandler,
    publishableChannel: Channel<Publishable>,
    coroutineDispatchers: CoroutineDispatchers,
    json: Json,
    wsPort: Int? = null,
    mqttPort: Int = 1883,
) : DIAware {
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)
    private val broker = Broker(
        authentication = auth,
        packetInterceptor = packetInterceptor,
        webSocketPort = wsPort,
        port = mqttPort,
    )

    init {
        coroutineScope.launch {
            for (element in publishableChannel) {
                val payload = element.payload
                if (payload == null) {
                    logger.debug { "Publishing - topic: ${element.topic}" }
                    send(element.topic, null)
                } else {
                    val serializer = json.serializersModule.serializer(payload::class.java)
                    val payloadString = json.encodeToString(serializer, payload)
                    logger.debug { "Publishing - topic: ${element.topic}, payload: $payloadString" }
                    send(element.topic, payloadString)
                }
            }
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
    fun send(topic: String, payload: String?) = broker.publish(
        retain = false,
        topicName = topic,
        qos = Qos.EXACTLY_ONCE,
        properties = null,
        payload = payload?.run { toByteArray().toUByteArray() },
    )
}
