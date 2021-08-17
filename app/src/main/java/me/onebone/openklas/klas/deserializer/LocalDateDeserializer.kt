/*
 * OpenKLAS
 * Copyright (C) 2020-2021 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.onebone.openklas.klas.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class LocalDateDeserializer: TypeResolvableJsonDeserializer<LocalDate> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LocalDate =
		parseLocalDate(json.asString)

	override fun getType(): Type =
		LocalDate::class.java
}

private val Formats = listOf(
	DateTimeFormatter.ofPattern("yyyy-MM-dd")
)

private fun parseLocalDate(str: String): LocalDate {
	return Formats.firstNotNullOfOrNull {
		LocalDate.parse(str, it)
	} ?: throw JsonParseException("cannot parse LocalDate string \"$str\"")
}

object LocalDateSerializer: KSerializer<LocalDate> {
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("ZonedDateTimeSerializer", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): LocalDate =
		parseLocalDate(decoder.decodeString())

	override fun serialize(encoder: Encoder, value: LocalDate) {
		throw NotImplementedError("serializing not implemented")
	}
}
