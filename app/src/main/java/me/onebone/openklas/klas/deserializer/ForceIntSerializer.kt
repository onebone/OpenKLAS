package me.onebone.openklas.klas.deserializer

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.contentOrNull

/**
 * Serializer that helps data to be read as an int forcefully.
 */
object ForceIntSerializer: JsonTransformingSerializer<Int>(Int.serializer()) {
	override fun transformDeserialize(element: JsonElement): JsonElement {
		val primitive = element as? JsonPrimitive ?: error("element should be primitive type")

		return JsonPrimitive(primitive.contentOrNull?.trim()?.toInt() ?: 0)
	}
}
