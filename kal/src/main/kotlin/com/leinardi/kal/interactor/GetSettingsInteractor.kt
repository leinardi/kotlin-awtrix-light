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

import com.leinardi.kal.model.Settings
import com.leinardi.kal.ui.theme.Theme
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class GetSettingsInteractor(override val di: DI) : DIAware {
    private val isEnergySavingTimeInteractor: IsEnergySavingTimeInteractor by di.instance()
    private val isNightInteractor: IsNightInteractor by di.instance()

    operator fun invoke(): Settings = Settings()
        .applyTheme()
        .applyEnergyProfile()

    private fun Settings.applyTheme(): Settings = if (isNightInteractor()) {
        copy(
            calendarHeaderColor = Theme.Night.calendarAccent,
            calendarBackgroundColor = Theme.Night.contentColor,
            weekdayActiveColor = Theme.Night.calendarAccent,
            weekdayInactiveColor = Theme.Night.contentColor,
            timeColor = Theme.Night.contentColor,
            dateColor = Theme.Night.contentColor,
        )
    } else {
        copy(
            calendarHeaderColor = Theme.Day.calendarAccent,
            calendarBackgroundColor = Theme.Day.contentColor,
            weekdayActiveColor = Theme.Day.calendarAccent,
            weekdayInactiveColor = Theme.Day.contentColor,
            timeColor = Theme.Day.contentColor,
            dateColor = Theme.Day.contentColor,
        )
    }

    private fun Settings.applyEnergyProfile(): Settings = if (isEnergySavingTimeInteractor()) {
        copy(
            brightness = 1,
            autoBrightness = false,
        )
    } else {
        copy(autoBrightness = true)
    }
}
