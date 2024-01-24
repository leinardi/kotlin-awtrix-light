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

package com.leinardi.kal.log

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory

val logger: KLogger by lazy { KotlinLogging.logger("KAL") }

object Logger {
    private const val LOG4J_PATTERN = "%highlight{%-5level: [%c] %msg%n%throwable}" +
        "{FATAL=bright_red, ERROR=bright_red, WARN=bright_yellow, INFO=bright_green, DEBUG=bright_white, TRACE=white}"

    fun configureLog4j(level: io.github.oshai.kotlinlogging.Level) {
        val builder = ConfigurationBuilderFactory.newConfigurationBuilder()
        val console = builder
            .newAppender("Stdout", "CONSOLE")
            .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
        console.add(builder.newLayout("PatternLayout").addAttribute("pattern", LOG4J_PATTERN))
        builder.add(console)
        builder.add(builder.newRootLogger(convertToLog4jLevel(level)).add(builder.newAppenderRef("Stdout")))
        Configurator.initialize(builder.build())
    }

    fun setRootLoggerLevel(level: io.github.oshai.kotlinlogging.Level) {
        Configurator.setLevel(LogManager.getRootLogger().name, convertToLog4jLevel(level))
    }

    private fun convertToLog4jLevel(level: io.github.oshai.kotlinlogging.Level) = when (level) {
        io.github.oshai.kotlinlogging.Level.TRACE -> Level.TRACE
        io.github.oshai.kotlinlogging.Level.DEBUG -> Level.DEBUG
        io.github.oshai.kotlinlogging.Level.INFO -> Level.INFO
        io.github.oshai.kotlinlogging.Level.WARN -> Level.WARN
        io.github.oshai.kotlinlogging.Level.ERROR -> Level.ERROR
        io.github.oshai.kotlinlogging.Level.OFF -> Level.OFF
    }
}
