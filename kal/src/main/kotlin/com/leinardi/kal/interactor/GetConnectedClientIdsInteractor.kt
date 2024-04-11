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

import com.leinardi.kal.mqtt.MqttServer
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class GetConnectedClientIdsInteractor(override val di: DI) : DIAware {
    private val mqttServer: MqttServer by di.instance()
    operator fun invoke(): Set<String> = mqttServer.getConnectedClientIds()
}
