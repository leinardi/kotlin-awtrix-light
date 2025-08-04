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

import com.leinardi.kal.event.EventHandler
import com.leinardi.kal.ext.asString
import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Button
import com.leinardi.kal.model.Event
import com.leinardi.kal.model.MotionEvent
import io.github.davidepianca98.mqtt.broker.interfaces.PacketInterceptor
import io.github.davidepianca98.mqtt.packets.MQTTPacket
import io.github.davidepianca98.mqtt.packets.mqtt.MQTTConnect
import io.github.davidepianca98.mqtt.packets.mqtt.MQTTDisconnect
import io.github.davidepianca98.mqtt.packets.mqtt.MQTTPublish
import io.github.davidepianca98.mqtt.packets.mqtt.MQTTSubscribe
import io.github.davidepianca98.mqtt.packets.mqttv4.MQTT4Pingreq
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalUnsignedTypes::class)
@Singleton
class MqttPacketHandler @Inject constructor(
    private val eventHandler: EventHandler,
    private val json: Json,
) : PacketInterceptor {
    override fun packetReceived(
        clientId: String,
        username: String?,
        password: UByteArray?,
        packet: MQTTPacket
    ) {
        when (packet) {
            is MQTT4Pingreq -> Unit
            is MQTTConnect -> handleMqttConnect(packet)
            is MQTTDisconnect -> handleMqttDisconnect()
            is MQTTPublish -> handleMqttPublish(packet)
            is MQTTSubscribe -> handleMqttSubscribe(packet)
            else -> logger.debug { "MQTTPacket: ${packet::class.java.simpleName}" }
        }
    }

    private fun handleMqttConnect(packet: MQTTConnect) {
        logger.info { "MQTTConnect - protocol: ${packet.protocolName}${packet.protocolVersion}, clientID: ${packet.clientID}" }
    }

    private fun handleMqttDisconnect() {
        logger.info { "MQTTDisconnect" }
    }

    private fun handleMqttSubscribe(packet: MQTTSubscribe) {
        logger.debug { "MQTTSubscribe - subscriptions: ${packet.subscriptions}" }
        packet.subscriptions.firstOrNull { it.topicFilter.endsWith("/settings") }?.let { subscription ->
            eventHandler.sendEvent(Event.SettingsAvailable(subscription.topicFilter.substringBefore("/")))
        }
    }

    private fun handleMqttPublish(packet: MQTTPublish) {
        logger.debug { "MQTTPublish - topic: ${packet.topicName}, payload: ${packet.payload?.asString()}" }
        val payload = packet.payload?.asString()
        if (payload != null) {
            val topic = packet.topicName.split("/")
            val clientId = topic.first()
            if (topic.size == 2 && topic.last() == TOPIC_STATS) {
                eventHandler.sendEvent(Event.StatsReceived(clientId, json.decodeFromString(payload)))
            } else {
                if (topic.size == 3) {
                    when (val topicLast = topic.last()) {
                        Button.LEFT.topicValue,
                        Button.RIGHT.topicValue,
                        Button.SELECT.topicValue -> eventHandler.sendEvent(
                            Event.ButtonPressed(
                                clientId = clientId,
                                button = Button.fromTopicValue(topicLast),
                                motionEvent = MotionEvent.fromPayloadValue(payload.toInt()),
                            ),
                        )

                        TOPIC_CURRENT_APP -> eventHandler.sendEvent(Event.CurrentApp(clientId = clientId, app = payload))
                    }
                }
            }
        }
    }

    companion object {
        private const val TOPIC_CURRENT_APP = "currentApp"
        private const val TOPIC_STATS = "stats"
    }
}
