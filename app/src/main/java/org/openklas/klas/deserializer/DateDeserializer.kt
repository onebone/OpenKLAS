package org.openklas.klas.deserializer

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

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.apache.commons.lang3.time.FastDateFormat
import java.lang.Exception
import java.lang.reflect.Type
import java.util.Date

class DateDeserializer: TypeResolvableJsonDeserializer<Date> {
	private val formats = arrayOf(
		FastDateFormat.getInstance(),
		FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS"),
		FastDateFormat.getInstance("yyyy-MM-dd HH:mm"),
		FastDateFormat.getInstance("yyyy-MM-dd")
	)

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Date {
		val string = json.asString!!

		for(format in formats) {
			try {
				return format.parse(string) ?: continue
			}catch(e: Exception) {
			}
		}

		throw JsonParseException("Unable to parse Date: \"$string\"")
	}

	override fun getType(): Type {
		return Date::class.java
	}
}
