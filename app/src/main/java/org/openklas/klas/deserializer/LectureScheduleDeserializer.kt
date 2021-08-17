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

import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.openklas.klas.model.LectureSchedule
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class LectureScheduleSerializer: JsonTransformingSerializer<LectureSchedule>(LectureSchedule.serializer()) {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LectureSchedule") {
		for(i in 1..4) {
			element<Int>("timeNo$i")
		}

		element<String>("code")
		element<String>("dayname1")
		element<String>("locHname")
	}

	override fun transformDeserialize(element: JsonElement): JsonElement {
		val obj = element.jsonObject

		val periods = mutableListOf<Int>()
		for(i in 1..4) {
			val time = obj["timeNo$i"]
			if(time != null && time !is JsonNull) {
				periods += time.jsonPrimitive.int
			}
		}

		val transformed = mutableMapOf<String, JsonElement>(
			"periods" to JsonArray(periods.map {
				JsonPrimitive(it)
			})
		)

		return JsonObject(obj.filterTo(transformed) {
			!it.key.startsWith("timeNo")
		})
	}

	override fun transformSerialize(element: JsonElement): JsonElement {
		throw UnsupportedOperationException("Serialization of LectureSchedule is not supported")
	}
}
