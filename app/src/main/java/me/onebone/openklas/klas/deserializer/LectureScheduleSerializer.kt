package me.onebone.openklas.klas.deserializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.onebone.openklas.klas.model.LectureSchedule

class LectureScheduleSerializer: KSerializer<LectureSchedule> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LectureSchedule") {
		element<String>("dayname1")
		element<String?>("locHname")
		element<String>("code")
		for(i in 1..4) {
			element<Int?>("timeNo$i")
		}
	}

	override fun deserialize(decoder: Decoder): LectureSchedule {
		val json = decoder as? JsonDecoder
			?: throw IllegalStateException("`decoder` should be JsonDecoder instance")

		val element = json.decodeJsonElement()
		val obj = element.jsonObject

		return LectureSchedule(
			day = obj["code"]!!.jsonPrimitive.content.trim().toInt(),
			dayLabel = obj["dayname1"]!!.jsonPrimitive.content,
			classroom = obj["locHname"]!!.jsonPrimitive.contentOrNull,
			periods = (1..4).map { "timeNo$it" }.mapNotNull { key ->
				obj[key]?.jsonPrimitive?.intOrNull
			}
		)
	}

	override fun serialize(encoder: Encoder, value: LectureSchedule) {
		throw NotImplementedError("serialize() for LectureScheduleSerializer is not implemented")
	}
}
