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

package com.leinardi.kal

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.leinardi.kal.coroutine.CoroutineDispatchers
import com.leinardi.kal.log.Logger
import com.leinardi.kal.log.logger
import com.leinardi.kal.mqtt.MqttServer
import com.leinardi.kal.scheduler.DayNightScheduler
import io.github.oshai.kotlinlogging.Level
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.net.BindException

class Kal(override val di: DI) : DIAware, CliktCommand() {
    private val logLevel: Level by option("-l", "--loglevel", help = "TBD")
        .choice(
            "TRACE" to Level.TRACE,
            "DEBUG" to Level.DEBUG,
            "INFO" to Level.INFO,
            "WARN" to Level.WARN,
            "ERROR" to Level.ERROR,
            "OFF" to Level.OFF,
        )
        .default(Level.INFO)
    private val coroutineDispatchers: CoroutineDispatchers by di.instance()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)
    private val dayNightScheduler: DayNightScheduler by di.instance()
    private val mqttServer: MqttServer by di.instance()
    private var running: Boolean = false

    override fun run() {
        Logger.setRootLoggerLevel(logLevel)
        logger.debug { "Kal run" }
        coroutineScope.launch { dayNightScheduler.start(this) }
        try {
            running = true
            mqttServer.start()
        } catch (e: BindException) {
            logger.error { e.message }
        } finally {
            running = false
        }
    }

    fun onCleared() {
        logger.debug { "Kal onCleared" }
        if (running) {
            mqttServer.stop()
            running = false
        }
    }
}
