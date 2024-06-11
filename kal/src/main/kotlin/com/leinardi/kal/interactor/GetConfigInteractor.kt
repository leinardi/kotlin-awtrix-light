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

import com.charleskorn.kaml.Yaml
import com.leinardi.kal.model.Config
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetConfigInteractor @Inject constructor(
    private val yaml: Yaml,
) {
    private val config: Config by lazy {
        readFile(CONFIG_FILE_PATH).let {
            if (it.isNullOrBlank()) Config() else yaml.decodeFromString(Config.serializer(), it)
        }
    }

    operator fun invoke(): Config = config

    private fun readFile(filePath: String): String? = try {
        File(filePath).readText(Charsets.UTF_8)
    } catch (e: IOException) {
        null
    }

    companion object {
        private const val CONFIG_FILE_PATH = "/etc/opt/kal/config.yaml"
    }
}
