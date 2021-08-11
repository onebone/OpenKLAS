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
import org.openklas.klas.model.LectureSchedule
import java.lang.reflect.Type

class LectureScheduleDeserializer: TypeResolvableJsonDeserializer<LectureSchedule> {
	override fun getType(): Type {
		return LectureSchedule::class.java
	}

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LectureSchedule {
		val obj = json.asJsonObject

		val periods = mutableListOf<Int>()
		for(i in 1..4) {
			if(!obj["timeNo$i"].isJsonNull) {
				periods += obj["timeNo$i"].asInt
			}
		}

		return LectureSchedule(
			day = obj["code"].asString.trim().toInt(),
			dayLabel = obj["dayname1"].asString,
			classroom = obj["locHname"].asStringOrNull,
			periods = periods
		)
	}
}
