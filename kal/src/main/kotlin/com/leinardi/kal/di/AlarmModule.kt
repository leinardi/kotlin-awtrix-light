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

import com.leinardi.kal.interactor.GetConfigInteractor
import com.leinardi.kal.scheduler.alarm.Alarm
import com.leinardi.kal.scheduler.alarm.BirthdayAlarm
import com.leinardi.kal.scheduler.alarm.EnergySavingEndPeriodAlarm
import com.leinardi.kal.scheduler.alarm.EnergySavingStartPeriodAlarm
import com.leinardi.kal.scheduler.alarm.NewYearAlarm
import com.leinardi.kal.scheduler.alarm.SunriseAlarm
import com.leinardi.kal.scheduler.alarm.SunsetAlarm
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet

@Module
object AlarmModule {
    @Provides
    @ElementsIntoSet
    fun providesBirthdayAlarm(
        birthdayAlarmFactory: BirthdayAlarm.Factory,
        getConfigInteractor: GetConfigInteractor,
    ): Set<Alarm> =
        getConfigInteractor().birthdayList.map { birthdayAlarmFactory.create(it) }.toSet()

    @Provides
    @IntoSet
    fun providesEnergySavingEndPeriodAlarm(alarm: EnergySavingEndPeriodAlarm): Alarm = alarm

    @Provides
    @IntoSet
    fun providesEnergySavingStartPeriodAlarm(alarm: EnergySavingStartPeriodAlarm): Alarm = alarm

    @Provides
    @IntoSet
    fun providesNewYearAlarm(alarm: NewYearAlarm): Alarm = alarm

    @Provides
    @IntoSet
    fun providesSunriseAlarm(alarm: SunriseAlarm): Alarm = alarm

    @Provides
    @IntoSet
    fun providesSunsetAlarm(alarm: SunsetAlarm): Alarm = alarm
}
