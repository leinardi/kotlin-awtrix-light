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

import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.parameters.options.eagerOption
import com.leinardi.kal.log.Logger
import io.github.oshai.kotlinlogging.Level
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        Logger.configureLog4j(Level.WARN)
        Kal().run {
            Runtime.getRuntime().addShutdownHook(Thread { onCleared() })
            eagerOption(
                names = setOf("--version"),
                help = "Show the version and exit",
            ) {
                throw PrintMessage(BuildConfig.VERSION)
            }
            main(args)
        }
    }
}
