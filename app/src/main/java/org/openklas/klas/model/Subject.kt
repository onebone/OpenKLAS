package org.openklas.klas.model

import com.google.gson.annotations.SerializedName

data class Subject (
	@SerializedName("bunban")
	val division: String,
	// codenmord
	// grcode
	@SerializedName("hakgi")
	val term: String,
	@SerializedName("hakjungno")
	val academicNumber: String,
	@SerializedName("profNm")
	val professor: String,
	@SerializedName("subj")
	val id: String,
	@SerializedName("subjNm")
	val name: String,
	@SerializedName("year")
	val year: Int,
	@SerializedName("yearhakgi")
	val semester: String,
	@SerializedName("openOrganCodeNm")
	val targetDegree: String,
	@SerializedName("iselearn")
	val isELearning: Boolean
)
