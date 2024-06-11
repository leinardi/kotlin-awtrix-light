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

import com.charleskorn.kaml.Yaml
import com.leinardi.kal.BaseTest
import com.leinardi.kal.di.TestComponent
import kotlinx.serialization.encodeToString
import java.time.LocalDate
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigTest : BaseTest() {
    @Inject lateinit var yaml: Yaml

    private val birthdayDataList = listOf(
        BirthdayData(
            dateOfBirth = LocalDate.parse("1980-01-01"),
            name = "John",
        ),
        BirthdayData(
            dateOfBirth = LocalDate.parse("1980-12-31"),
            name = "Doe",
        ),
    )

    override fun inject(testComponent: TestComponent) {
        testComponent.inject(this)
    }

    @Test
    fun `Test serialization and deserialization`() {
        val config = Config(birthdayDataList)

        val yamlString = yaml.encodeToString(config)
        assertEquals(config, yaml.decodeFromString(Config.serializer(), yamlString))
    }

    @Test
    fun `Test deserialization of partial config`() {
        val config = Config(birthdayDataList)
        val yamlString = readTextFileFromResources("config-partial.yaml")
        assertEquals(config, yaml.decodeFromString(Config.serializer(), yamlString))
    }
}
