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

package com.leinardi.kal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val app: String,
    val bat: Int,
    @SerialName("bat_raw")
    val batRaw: Int,
    val bri: Int,
    val hum: Int,
    val indicator1: Boolean,
    val indicator2: Boolean,
    val indicator3: Boolean,
    @SerialName("ip_address")
    val ipAddress: String,
    @SerialName("ldr_raw")
    val ldrRaw: Int,
    val lux: Int,
    val matrix: Boolean,
    val messages: Int,
    val ram: Int,
    val temp: Int,
    val type: Int,
    val uid: String,
    val uptime: Int,
    val version: String,
    @SerialName("wifi_signal")
    val wifiSignal: Int
)
