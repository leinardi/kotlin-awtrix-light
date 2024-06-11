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

package com.leinardi.kal.interactor

import com.leinardi.kal.log.logger
import com.leinardi.kal.model.Publishable
import com.leinardi.kal.mqtt.MqttServer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class PublishInteractor @Inject constructor(
    private val mqttServer: Provider<MqttServer>,
    private val json: Json,
) {
    suspend operator fun invoke(publishable: Publishable): Boolean {
        val payload = publishable.payload
        return if (payload == null) {
            logger.debug { "Publishing - topic: ${publishable.topic}" }
            mqttServer.get().send(publishable.topic, null)
        } else {
            val serializer = json.serializersModule.serializer(payload::class.java)
            val payloadString = json.encodeToString(serializer, payload)
            logger.debug { "Publishing - topic: ${publishable.topic}, payload: $payloadString" }
            mqttServer.get().send(publishable.topic, payloadString)
        }
    }
}
