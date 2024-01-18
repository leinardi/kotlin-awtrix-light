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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    @SerialName("ATIME")
    val appTime: Int? = null,
    @SerialName("TEFF")
    val transitionEffect: TransitionEffect? = null,
    @SerialName("TSPEED")
    val transitionSpeed: Int? = null,
    @SerialName("TCOL")
    val transitionColor: Color? = null,
    @SerialName("TMODE")
    val timeMode: TimeMode? = null,
    @SerialName("CHCOL")
    val calendarHeaderColor: Color? = null,
    @SerialName("CBCOL")
    val calendarBackgroundColor: Color? = null,
    @SerialName("CTCOL")
    val calendarTextColor: Color? = null,
    @SerialName("WD")
    val weekdayDisplay: Boolean? = null,
    @SerialName("WDCA")
    val weekdayActiveColor: Color? = null,
    @SerialName("WDCI")
    val weekdayInactiveColor: Color? = null,
    @SerialName("BRI")
    val brightness: Int? = null,
    @SerialName("ABRI")
    val autoBrightness: Boolean? = null,
    @SerialName("ATRANS")
    val autoTransition: Boolean? = null,
    @SerialName("CCORRECTION")
    val colorCorrection: List<Int>? = null,
    @SerialName("CTEMP")
    val colorTemperature: List<Int>? = null,
    @SerialName("TFORMAT")
    val timeFormat: TimeFormat? = null,
    @SerialName("DFORMAT")
    val dateFormat: DateFormat? = null,
    @SerialName("SOM")
    val startOfWeekOnMonday: Boolean? = null,
    @SerialName("BLOCKN")
    val blocKeysNavigation: Boolean? = null,
    @SerialName("UPPERCASE")
    val uppercase: Boolean? = null,
    @SerialName("TIME_COL")
    val timeColor: Color? = null,
    @SerialName("DATE_COL")
    val dateColor: Color? = null,
    @SerialName("TEMP_COL")
    val temperatureColor: Color? = null,
    @SerialName("HUM_COL")
    val humidityColor: Color? = null,
    @SerialName("BAT_COL")
    val batteryColor: Color? = null,
    @SerialName("SSPEED")
    val scrollSpeed: Int? = null,
    @SerialName("TIM")
    val timeApp: Boolean? = null,
    @SerialName("DAT")
    val dateApp: Boolean? = null,
    @SerialName("HUM")
    val humidityApp: Boolean? = null,
    @SerialName("TEMP")
    val temperatureApp: Boolean? = null,
    @SerialName("BAT")
    val batteryApp: Boolean? = null,
    @SerialName("MATP")
    val matrixEnabled: Boolean? = null,
    @SerialName("VOL")
    val volumeDfplayer: Int? = null,
) {
    enum class TransitionEffect {
        @SerialName("0")
        RANDOM,

        @SerialName("1")
        SLIDE,

        @SerialName("2")
        DIM,

        @SerialName("3")
        ZOOM,

        @SerialName("4")
        ROTATE,

        @SerialName("5")
        PIXELATE,

        @SerialName("6")
        CURTAIN,

        @SerialName("7")
        RIPPLE,

        @SerialName("8")
        BLINK,

        @SerialName("9")
        RELOAD,

        @SerialName("10")
        FADE
    }

    enum class TimeMode {
        @SerialName("0")
        MODE0,

        @SerialName("1")
        MODE1,

        @SerialName("2")
        MODE2,

        @SerialName("3")
        MODE3,

        @SerialName("4")
        MODE4,
    }

    enum class TimeFormat {
        /**
         * e.g. 13:30:45
         */
        @SerialName("%H:%M:%S")
        FORMAT0,

        /**
         * e.g. 1:30:45
         */
        @SerialName("%l:%M:%S")
        FORMAT1,

        /**
         * e.g. 13:30
         */
        @SerialName("%H:%M")
        FORMAT2,

        /**
         * e.g. 13.30 with blinking colon
         */
        @SerialName("%H %M")
        FORMAT3,

        /**
         * e.g. 1:30
         */
        @SerialName("%l:%M")
        FORMAT4,

        /**
         * e.g. 1:30 with blinking colon
         */
        @SerialName("%l %M")
        FORMAT5,

        /**
         * e.g. 1:30 PM
         */
        @SerialName("%l:%M %p")
        FORMAT6,

        /**
         * e.g. 1:30 PM with blinking colon
         */
        @SerialName("%l %M %p")
        FORMAT7,
    }

    enum class DateFormat {
        /**
         * e.g. 16.04.22
         */
        @SerialName("%d.%m.%y")
        FORMAT0,

        /**
         * e.g. 16.04
         */
        @SerialName("%d.%m")
        FORMAT1,

        /**
         * e.g. 22-04-16
         */
        @SerialName("%y-%m-%d")
        FORMAT2,

        /**
         * e.g. 04-16
         */
        @SerialName("%m-%d")
        FORMAT3,

        /**
         * e.g. 04/16/22
         */
        @SerialName("%m/%d/%y")
        FORMAT4,

        /**
         * e.g. 04/16
         */
        @SerialName("%m/%d")
        FORMAT5,

        /**
         * e.g. 16/04/22
         */
        @SerialName("%d/%m/%y")
        FORMAT6,

        /**
         * e.g. 16/04
         */
        @SerialName("%d/%m")
        FORMAT7,

        /**
         * e.g. 04-16-22
         */
        @SerialName("%m-%d-%y")
        FORMAT8,
    }
}
