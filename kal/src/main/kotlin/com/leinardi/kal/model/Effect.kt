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

enum class Effect {
    @SerialName("Fade")
    FADE,

    @SerialName("MovingLine")
    MOVING_LINE,

    @SerialName("BrickBreaker")
    BRICK_BREAKER,

    @SerialName("PingPong")
    PING_PONG,

    @SerialName("Radar")
    RADAR,

    @SerialName("Checkerboard")
    CHECKERBOARD,

    @SerialName("Fireworks")
    FIREWORKS,

    @SerialName("PlasmaCloud")
    PLASMA_CLOUD,

    @SerialName("Ripple")
    RIPPLE,

    @SerialName("Snake")
    SNAKE,

    @SerialName("Pacifica")
    PACIFICA,

    @SerialName("TheaterChase")
    THEATER_CHASE,

    @SerialName("Plasma")
    PLASMA,

    @SerialName("Matrix")
    MATRIX,

    @SerialName("SwirlIn")
    SWIRL_IN,

    @SerialName("SwirlOut")
    SWIRL_OUT,

    @SerialName("LookingEyes")
    LOOKING_EYES,

    @SerialName("TwinklingStars")
    TWINKLING_STARS,

    @SerialName("ColorWaves")
    COLOR_WAVES,
}
