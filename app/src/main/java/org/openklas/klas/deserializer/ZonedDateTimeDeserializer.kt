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

package org.openklas.klas.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeDeserializer: TypeResolvableJsonDeserializer<ZonedDateTime> {
	private companion object {
		val KoreaZoneId: ZoneId = ZoneId.of("Asia/Seoul")

		val Formats = listOf(
			DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS[Z]"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH시mm분")
		)
	}

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): ZonedDateTime {
		val str = json.asString

		return when(val temporal = Formats.firstNotNullOfOrNull {
			kotlin.runCatching {
				it.parseBest(str, ZonedDateTime::from, LocalDateTime::from)
			}.getOrNull()
		} ?: throw JsonParseException("unable to parse time string: \"$str\"")) {
			is ZonedDateTime -> temporal
			is LocalDateTime -> temporal.atZone(KoreaZoneId)
			else -> throw IllegalStateException("this should not happen")
		}
	}

	override fun getType(): Type =
		ZonedDateTime::class.java
}
