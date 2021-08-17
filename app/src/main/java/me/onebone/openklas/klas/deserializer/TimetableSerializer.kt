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

import me.onebone.openklas.klas.model.Timetable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

class TimetableSerializer: KSerializer<Timetable> {
	@OptIn(ExperimentalSerializationApi::class)
	override val descriptor: SerialDescriptor =
		listSerialDescriptor(buildClassSerialDescriptor("Entry") {
			repeat(6) {
				val dayOfWeek = it + 1
				element<Int>("wtSpan_$dayOfWeek", isOptional = true)
				// surprisingly, this property could yield null
				element<String?>("wtProfNm_$dayOfWeek", isOptional = true)

				// though it can't be expressed in the descriptor, properties below should exist if
				// wtSpan_$it exists
				element<String>("wtLocHname_$dayOfWeek", isOptional = true)
				element<String>("wtSubj_$dayOfWeek", isOptional = true)
				element<String>("wtSubjNm_$dayOfWeek", isOptional = true)
				element<Int>("wtSubjPrintSeq_$dayOfWeek", isOptional = true)
				element<String>("wtYearhakgi_$dayOfWeek", isOptional = true)
			}

			element<Int>("wtTime")
		})

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Timetable {
		// blame json structure for this complexity!!
		val entryMap = mutableMapOf<Int, MutableMap<Int, BuildingEntry>>()

		decoder.decodeStructure(descriptor) {
			while(true) {
				val index = decodeElementIndex(descriptor)
				if(index == CompositeDecoder.DECODE_DONE) break

				val innerDecoder = decodeInlineElement(descriptor, index)
				val innerDescriptor = descriptor.getElementDescriptor(index)
				innerDecoder.decodeStructure(innerDescriptor) {
					val entries = entryMap[index] ?: mutableMapOf()
					var wtTime: Int? = null

					while(true) {
						val innerIndex = decodeElementIndex(innerDescriptor)
						if(innerIndex == CompositeDecoder.DECODE_DONE) break

						if(innerIndex == 42) { // wtTime
							wtTime = decodeIntElement(innerDescriptor, 42)
							entries.forEach { (_, entry) ->
								entry.time = wtTime
							}
							continue
						}

						val key = innerIndex / 7
						val entry = entries[key] ?: BuildingEntry()
						when(innerIndex % 7) {
							0 -> entry.span = decodeIntElement(innerDescriptor, innerIndex)
							1 -> entry.professor = decodeStringElement(innerDescriptor, innerIndex)
							2 -> entry.classroom = decodeStringElement(innerDescriptor, innerIndex)
							3 -> entry.subjectId = decodeStringElement(innerDescriptor, innerIndex)
							4 -> entry.subjectName = decodeStringElement(innerDescriptor, innerIndex)
							5 -> entry.printSeq = decodeIntElement(innerDescriptor, innerIndex)
							6 -> entry.semester = decodeStringElement(innerDescriptor, innerIndex)
						}

						if(wtTime != null) entry.time = wtTime
						entries[key] = entry
					}

					entryMap[index] = entries
				}
			}
		}

		return Timetable(entryMap.flatMap { (_, entries) ->
			entries.map { (day, entry) ->
				Timetable.Entry(
					day = day + 1, time = entry.time!!, classroom = entry.classroom!!, professor = entry.professor,
					length = entry.span!!, subjectName = entry.subjectName!!, subjectId = entry.subjectId!!,
					semester = entry.semester!!, printSeq = entry.printSeq!!
				)
			}
		})
	}

	override fun serialize(encoder: Encoder, value: Timetable) {
		throw NotImplementedError("Timetable does not need to be serialized at the moment")
	}

	private data class BuildingEntry(
		var span: Int? = null,
		var time: Int? = null,
		var professor: String? = null,
		var classroom: String? = null,
		var subjectId: String? = null,
		var subjectName: String? = null,
		var printSeq: Int? = null,
		var semester: String? = null
	)
}
