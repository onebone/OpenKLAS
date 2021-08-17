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
import me.onebone.openklas.klas.model.OnlineContentEntry
import java.lang.reflect.Type
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonElement as SerializationJsonElement
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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
			"quiz" -> context.deserialize<OnlineContentEntry.Quiz>(obj, OnlineContentEntry.Quiz::class.java)
			else -> context.deserialize<OnlineContentEntry.Dummy>(obj, OnlineContentEntry.Dummy::class.java)
		}
	}

	override fun getType(): Type {
		return OnlineContentEntry::class.java
	}
}

object OnlineContentEntrySerializer: JsonContentPolymorphicSerializer<OnlineContentEntry>(OnlineContentEntry::class) {
	override fun selectDeserializer(
		element: SerializationJsonElement
	): DeserializationStrategy<out OnlineContentEntry> {
		val obj = element.jsonObject
		return when(obj["evltnSe"]?.jsonPrimitive?.contentOrNull) {
			"lesson" -> OnlineContentEntry.Video.serializer()
			"proj" -> OnlineContentEntry.Homework.serializer()
			"quiz" -> OnlineContentEntry.Quiz.serializer()
			else -> OnlineContentEntry.Dummy.serializer()
		}
	}
}
