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

import com.leinardi.kal.ext.asString
import mqtt.broker.interfaces.Authentication

@OptIn(ExperimentalUnsignedTypes::class)
class MqttAuthenticator : Authentication {
    override fun authenticate(clientId: String, username: String?, password: UByteArray?): Boolean =
        username == "user" && password?.asString() == "pass"
}
