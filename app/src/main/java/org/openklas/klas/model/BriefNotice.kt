package org.openklas.klas.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BriefNotice (
	@SerializedName("bunban")
	val division: String,
	// grcode?
	val param: String,
	@SerializedName("hakgi")
	val term: Int,
	@SerializedName("registDt")
	val postDate: Date,
	@SerializedName("subj")
	val subjectId: String,
	@SerializedName("subjNm")
	val subjectName: String,
	@SerializedName("title")
	val title: String,
	val type: Int,
	@SerializedName("typeNm")
	val typeName: String,
	@SerializedName("year")
	val year: Int,
	@SerializedName("yearhakgi")
	val semester: String
)
