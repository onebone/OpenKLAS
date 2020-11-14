package org.openklas.klas.model

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class Home (
	@SerializedName("atnlcSbjectList")
	val subjects: Array<Subject>,
	@SerializedName("rspnsblProfsr")
	val professor: Professor,
	@SerializedName("subjNotiList")
	val notices: Array<BriefNotice>,
	@SerializedName("yearhakgi")
	val semesterLabel: String,
	@SerializedName("timeTableList")
	val timetable: Timetable
)
