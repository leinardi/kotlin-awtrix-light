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

import com.leinardi.kal.scheduler.alarm.BirthdayAlarm
import com.leinardi.kal.scheduler.alarm.EnergySavingEndPeriodAlarm
import com.leinardi.kal.scheduler.alarm.EnergySavingStartPeriodAlarm
import com.leinardi.kal.scheduler.alarm.NewYearAlarm
import com.leinardi.kal.scheduler.alarm.SunriseAlarm
import com.leinardi.kal.scheduler.alarm.SunsetAlarm
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.quartz.Job

@Module
object JobModule {
    @Provides
    @IntoMap
    @JobClassKey(BirthdayAlarm.BirthdayJob::class)
    fun provideBirthdayJob(job: BirthdayAlarm.BirthdayJob): Job = job

    @Provides
    @IntoMap
    @JobClassKey(EnergySavingEndPeriodAlarm.EnergySavingEndPeriodJob::class)
    fun provideEnergySavingEndPeriodJob(job: EnergySavingEndPeriodAlarm.EnergySavingEndPeriodJob): Job = job

    @Provides
    @IntoMap
    @JobClassKey(EnergySavingStartPeriodAlarm.EnergySavingStartPeriodJob::class)
    fun provideEnergySavingStartPeriodJob(job: EnergySavingStartPeriodAlarm.EnergySavingStartPeriodJob): Job = job

    @Provides
    @IntoMap
    @JobClassKey(NewYearAlarm.NewYearJob::class)
    fun provideNewYearJob(job: NewYearAlarm.NewYearJob): Job = job

    @Provides
    @IntoMap
    @JobClassKey(SunriseAlarm.SunriseJob::class)
    fun provideSunriseJob(job: SunriseAlarm.SunriseJob): Job = job

    @Provides
    @IntoMap
    @JobClassKey(SunsetAlarm.SunsetJob::class)
    fun provideSunsetJob(job: SunsetAlarm.SunsetJob): Job = job
}
