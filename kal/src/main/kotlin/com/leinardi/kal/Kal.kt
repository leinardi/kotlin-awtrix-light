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
import com.leinardi.kal.di.DaggerKalComponent
import com.leinardi.kal.ext.scheduleJob
import com.leinardi.kal.interactor.GetConfigInteractor
import com.leinardi.kal.log.Logger
import com.leinardi.kal.log.logger
import com.leinardi.kal.mqtt.MqttServer
import com.leinardi.kal.scheduler.DaggerJobFactory
import com.leinardi.kal.scheduler.LogSchedulerListener
import com.leinardi.kal.scheduler.alarm.Alarm
import io.github.oshai.kotlinlogging.Level
import org.quartz.Scheduler
import java.net.BindException
import javax.inject.Inject
import javax.inject.Provider

class Kal : CliktCommand() {
    @Inject lateinit var alarms: Set<@JvmSuppressWildcards Alarm>

    @Inject lateinit var daggerJobFactory: DaggerJobFactory

    @Inject lateinit var getConfigInteractor: GetConfigInteractor

    @Inject lateinit var mqttServer: Provider<MqttServer>

    @Inject lateinit var scheduler: Scheduler

    init {
        DaggerKalComponent.create().inject(this)
    }

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

    private var running: Boolean = false

    override fun run() {
        Logger.setRootLoggerLevel(logLevel)
        logger.debug { "Kal run" }
        logger.debug { "Configuration loaded: ${getConfigInteractor()}" }
        initScheduler()
        try {
            running = true
            mqttServer.get().start()
        } catch (e: BindException) {
            logger.error { e.message }
        } finally {
            running = false
        }
    }

    private fun initScheduler() {
        scheduler.listenerManager.addSchedulerListener(LogSchedulerListener())
        scheduler.setJobFactory(daggerJobFactory)
        scheduler.start()
        alarms.forEach { scheduler.scheduleJob(it) }
    }

    fun onCleared() {
        logger.debug { "Kal onCleared" }
        scheduler.shutdown()
        if (running) {
            mqttServer.get().stop()
            running = false
        }
    }
}
