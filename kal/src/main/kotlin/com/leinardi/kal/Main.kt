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

import com.leinardi.kal.di.build
import com.leinardi.kal.log.configureLog4j
import io.github.oshai.kotlinlogging.Level
import kotlinx.coroutines.runBlocking
import org.kodein.di.DI

fun main() {
    runBlocking {
        configureLog4j(Level.DEBUG)
        val kodein = DI { build() }
        Kal(kodein).run {
            Runtime.getRuntime().addShutdownHook(Thread { onCleared() })
            onCreate()
        }
    }
}
