package org.openklas.klas.model

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class Semester (
	@SerializedName("value")
	val id: String,
	val label: String,
	@SerializedName("subjList")
	val subjects: Array<BriefSubject>
)
