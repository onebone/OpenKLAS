package org.openklas.klas.model

import com.google.gson.annotations.SerializedName

data class Professor (
	@SerializedName("telNum")
	val telephone: String,
	@SerializedName("labLocation")
	val lab: String,
	@SerializedName("kname")
	val koreanName: String,
	@SerializedName("email")
	val email: String,
	@SerializedName("counselTime")
	val counselTime: String?
)
