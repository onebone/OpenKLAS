package org.openklas.klas.model

import com.google.gson.annotations.SerializedName

data class BriefSubject (
	@SerializedName("value")
	val id: String,
	val label: String,
	val name: String
)
