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
import org.openklas.klas.model.Timetable
import java.lang.reflect.Type

class TimetableDeserializer: TypeResolvableJsonDeserializer<Timetable> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Timetable {
		return Timetable(json.asJsonArray.flatMap {
			parseEntries(it)
		}.toTypedArray())
	}

	private fun parseEntries(element: JsonElement): List<Timetable.Entry> {
		val obj = element.asJsonObject

		val entries = mutableListOf<Timetable.Entry>()
		for(i in 1..6) {
			val length = obj["wtSpan_$i"]?.asInt ?: continue
			if(length <= 0) continue

			// surprisingly, this property could yield null
			val professor = obj["wtProfNm_$i"].asStringOrNull ?: ""

			// properties below must have value
			val classroom = obj["wtLocHname_$i"].asString
			val subjectName = obj["wtSubjNm_$i"].asString
			val subjectId = obj["wtSubj_$i"].asString
			val printSeq = obj["wtSubjPrintSeq_$i"].asInt
			val semester = obj["wtYearhakgi_$i"].asString

			entries += Timetable.Entry(
				i, classroom, professor, length, subjectName,
				subjectId, semester, printSeq
			)
		}

		return entries
	}

	override fun getType(): Type {
		return Timetable::class.java
	}
}
