package org.openklas.klas.deserializer

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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
import java.lang.Exception
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateDeserializer: TypeResolvableJsonDeserializer<Date> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Date {
		val string = json.asString!!

		// SimpleDateFormat is NOT thread-safe.
		// DateDeserializer might parse multiple Date strings simultaneously,
		// so date format objects must be created at the time of formatting.
		val formats = arrayOf(
			SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault()),
			SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
		)

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
