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

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val text: String? = null,
    val textCase: Int? = null,
    val topText: Boolean? = null,
    val textOffset: Int? = null,
    val center: Boolean? = null,
    val color: Color? = null,
    val gradient: List<Color>? = null,
    val blinkText: Int? = null,
    val fadeText: Int? = null,
    val background: Color? = null,
    val rainbow: Boolean? = null,
    val icon: String? = null,
    val pushIcon: Int? = null,
    val repeat: Int? = null,
    val duration: Int? = null,
    val hold: Boolean? = null,
    val sound: String? = null,
    val rtttl: String? = null,
    val loopSound: Boolean? = null,
    val bar: List<Int>? = null,
    val line: List<Int>? = null,
    val autoscale: Boolean? = null,
    val progress: Int? = null,
    val progressC: Color? = null,
    val progressBC: Color? = null,
    val draw: List<Draw>? = null,
    val stack: Boolean? = null,
    val wakeup: Boolean? = null,
    val noScroll: Boolean? = null,
    val clients: List<String>? = null,
    val scrollSpeed: Int? = null,
    val effect: Effect? = null,
    val effectSettings: EffectSettings? = null,
    val overlay: OverlayEffect? = null,
)
