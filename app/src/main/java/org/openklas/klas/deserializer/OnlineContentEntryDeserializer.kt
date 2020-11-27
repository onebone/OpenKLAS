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
import org.openklas.klas.model.OnlineContentEntry
import java.lang.reflect.Type

class OnlineContentEntryDeserializer: TypeResolvableJsonDeserializer<OnlineContentEntry> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext
	): OnlineContentEntry {
		val obj = json.asJsonObject

		return when(obj["evltnSe"].asString) {
			"proj" -> context.deserialize<OnlineContentEntry.Homework>(obj, OnlineContentEntry.Homework::class.java)
			"lesson" -> context.deserialize<OnlineContentEntry.Video>(obj, OnlineContentEntry.Video::class.java)
			else -> context.deserialize<OnlineContentEntry.Dummy>(obj, OnlineContentEntry.Dummy::class.java)
		}
	}

	override fun getType(): Type {
		return OnlineContentEntry::class.java
	}
}
