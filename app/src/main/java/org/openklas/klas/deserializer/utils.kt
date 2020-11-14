package org.openklas.klas.deserializer

import com.google.gson.JsonElement
import com.google.gson.JsonNull

val JsonElement.asStringOrNull: String?
	get() =
		if(this is JsonNull) null
		else this.asString
