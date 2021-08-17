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
import java.lang.reflect.Type
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class YNBoolDeserializer: TypeResolvableJsonDeserializer<Boolean> {
	override fun getType(): Type {
		return Boolean::class.java
	}

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Boolean {
		return json.asString == "Y"
	}
}

object YNBoolSerializer: KSerializer<Boolean> {
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("YNBool", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): Boolean = when(decoder.decodeString()) {
		"Y" -> true
		else -> false
	}

	override fun serialize(encoder: Encoder, value: Boolean): Unit = encoder.encodeString(
		if(value) "Y" else "N"
	)
}
