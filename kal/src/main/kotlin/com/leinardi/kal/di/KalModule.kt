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

package com.leinardi.kal.di

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.leinardi.kal.coroutine.CoroutineDispatchers
import com.leinardi.kal.serializer.LocalDateSerializer
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
object KalModule {
    @Provides
    @Singleton
    fun providesCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers()

    @Provides
    @Singleton
    fun provideSerializersModule() = SerializersModule {
        contextual(LocalDate::class, LocalDateSerializer)
    }

    @Provides
    @Singleton
    fun providesJson(serializerModule: SerializersModule): Json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        serializersModule = serializerModule
    }

    @Provides
    @Singleton
    fun providesYaml(serializerModule: SerializersModule): Yaml = Yaml(
        serializersModule = serializerModule,
        configuration = YamlConfiguration(
            encodeDefaults = true,
        ),
    )

    @Provides
    @Singleton
    fun providesScheduler(): Scheduler = StdSchedulerFactory.getDefaultScheduler()
}
