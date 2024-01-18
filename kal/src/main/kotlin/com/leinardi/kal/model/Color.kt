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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Locale

@Serializable(with = ColorSerializer::class)
class Color(private val color: Int) {
    init {
        require(isValidColor(color)) { "Invalid color: $color. Only RGB without alpha is allowed." }
    }

    private fun isValidColor(rgb: Int): Boolean {
        val maxColorValue = 0xFFFFFF // Maximum RGB value without alpha
        return rgb and maxColorValue.inv() == 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Color
        return color == other.color
    }

    override fun hashCode(): Int = color

    override fun toString(): String = String.format(Locale.ROOT, "#%06X", color)

    companion object {
        fun parseColor(colorString: String): Color {
            require(colorString.matches(Regex("#[0-9A-Fa-f]{6}"))) { "Invalid HTML color code: $colorString" }
            return Color(colorString.substring(1).toInt(16))
        }
    }
}

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ColorSerializer")

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Color = Color.parseColor(decoder.decodeString())
}
