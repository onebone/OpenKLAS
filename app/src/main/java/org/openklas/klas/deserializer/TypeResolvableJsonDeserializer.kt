package org.openklas.klas.deserializer

import com.google.gson.JsonDeserializer
import java.lang.reflect.Type

interface TypeResolvableJsonDeserializer<T>: JsonDeserializer<T> {
	fun getType(): Type
}
