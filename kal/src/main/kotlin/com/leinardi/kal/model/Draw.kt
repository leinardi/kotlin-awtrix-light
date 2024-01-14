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

@file:OptIn(ExperimentalSerializationApi::class)

package com.leinardi.kal.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.full.findAnnotation

@Serializable(with = DrawSerializer::class)
@SerialName("AAA")
sealed class Draw {
    @SerialName("dp")
    data class Pixel(
        val x: Int,
        val y: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("dl")
    data class Line(
        val x0: Int,
        val y0: Int,
        val x1: Int,
        val y1: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("dr")
    data class Rectangle(
        val x: Int,
        val y: Int,
        @SerialName("w")
        val width: Int,
        @SerialName("h")
        val height: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("df")
    data class FilledRectangle(
        val x: Int,
        val y: Int,
        @SerialName("w")
        val width: Int,
        @SerialName("h")
        val height: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("dc")
    data class Circle(
        val x: Int,
        val y: Int,
        @SerialName("r")
        val radius: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("dfc")
    data class FilledCircle(
        val x: Int,
        val y: Int,
        @SerialName("r")
        val radius: Int,
        @SerialName("cl")
        val color: String,
    ) : Draw()

    @SerialName("dt")
    data class Text(
        val x: Int,
        val y: Int,
        @SerialName("t")
        val text: String,
        @SerialName("cl")
        val color: String,
    ) : Draw()
}

object DrawSerializer : KSerializer<Draw> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DrawSerializer")

    override fun serialize(encoder: Encoder, value: Draw) {
        encoder as JsonEncoder
        encoder.encodeJsonElement(serializeToJsonElement(value))
    }

    override fun deserialize(decoder: Decoder): Draw {
        decoder as JsonDecoder
        val jsonElement = decoder.decodeJsonElement()
        return deserializeFromJsonElement(jsonElement)
    }

    @Suppress("LongMethod")
    private fun serializeToJsonElement(value: Draw): JsonElement = buildJsonObject {
        when (value) {
            is Draw.Circle -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.radius))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.FilledCircle -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.radius))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.FilledRectangle -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.width))
                    add(JsonPrimitive(value.height))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.Line -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x0))
                    add(JsonPrimitive(value.y0))
                    add(JsonPrimitive(value.x1))
                    add(JsonPrimitive(value.y1))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.Pixel -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.Rectangle -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.width))
                    add(JsonPrimitive(value.height))
                    add(JsonPrimitive(value.color))
                },
            )

            is Draw.Text -> put(
                key = value::class.findAnnotation<SerialName>()?.value ?: checkNotNull(value::class.simpleName),
                element = buildJsonArray {
                    add(JsonPrimitive(value.x))
                    add(JsonPrimitive(value.y))
                    add(JsonPrimitive(value.text))
                    add(JsonPrimitive(value.color))
                },
            )
        }
    }

    private fun deserializeFromJsonElement(jsonElement: JsonElement): Draw {
        val key = (jsonElement as JsonObject).keys.first()
        val value = jsonElement.values.first() as JsonArray
        return when (key) {
            Draw.Circle::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.Circle::class.simpleName) -> Draw.Circle(
                x = value[0].jsonPrimitive.int,
                y = value[1].jsonPrimitive.int,
                radius = value[2].jsonPrimitive.int,
                color = value[3].jsonPrimitive.content,
            )

            Draw.FilledCircle::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.FilledCircle::class.simpleName) -> Draw.FilledCircle(
                x = value[0].jsonPrimitive.int,
                y = value[1].jsonPrimitive.int,
                radius = value[2].jsonPrimitive.int,
                color = value[3].jsonPrimitive.content,
            )

            Draw.FilledRectangle::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.FilledRectangle::class.simpleName) ->
                Draw.FilledRectangle(
                    x = value[0].jsonPrimitive.int,
                    y = value[1].jsonPrimitive.int,
                    width = value[2].jsonPrimitive.int,
                    height = value[3].jsonPrimitive.int,
                    color = value[4].jsonPrimitive.content,
                )

            Draw.Line::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.Line::class.simpleName) -> Draw.Line(
                x0 = value[0].jsonPrimitive.int,
                y0 = value[1].jsonPrimitive.int,
                x1 = value[2].jsonPrimitive.int,
                y1 = value[3].jsonPrimitive.int,
                color = value[4].jsonPrimitive.content,
            )

            Draw.Pixel::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.Pixel::class.simpleName) -> Draw.Pixel(
                x = value[0].jsonPrimitive.int,
                y = value[1].jsonPrimitive.int,
                color = value[2].jsonPrimitive.content,
            )

            Draw.Rectangle::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.Rectangle::class.simpleName) -> Draw.Rectangle(
                x = value[0].jsonPrimitive.int,
                y = value[1].jsonPrimitive.int,
                width = value[2].jsonPrimitive.int,
                height = value[3].jsonPrimitive.int,
                color = value[4].jsonPrimitive.content,
            )

            Draw.Text::class.findAnnotation<SerialName>()?.value ?: checkNotNull(Draw.Text::class.simpleName) -> Draw.Text(
                x = value[0].jsonPrimitive.int,
                y = value[1].jsonPrimitive.int,
                text = value[2].jsonPrimitive.content,
                color = value[3].jsonPrimitive.content,
            )

            else -> error("Invalid JSON structure for Draw")
        }
    }
}
