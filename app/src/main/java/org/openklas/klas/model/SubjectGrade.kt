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

package org.openklas.klas.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.openklas.klas.deserializer.YNBoolDeserializer

data class SemesterGrade(
	@SerializedName("hakgi")
	val term: Int,
	@SerializedName("thisYear")
	val year: Int,
	@SerializedName("hakgiOrder")
	val semesterLabel: String,
	@SerializedName("sungjukList")
	val grades: List<SubjectGrade>
)

@JvmInline
value class Grade(
	private val value: String
) {
	val hasSettled: Boolean
		get() = value in AvailableGrades

	val grade: String?
		get() = AvailableGrades.find { value.startsWith(it) }

	val gpa: Double?
		get() = GpaMap[grade]

	val isGpaCounted: Boolean
		get() = grade in GpaMap

	companion object {
		private val GpaMap = mapOf(
			"A+" to 4.5,
			"A0" to 4.0,
			"B+" to 3.5,
			"B0" to 3.0,
			"C+" to 2.5,
			"C0" to 2.0,
			"D+" to 1.5,
			"D0" to 1.0,
			"F" to 0.0
		)

		private val AvailableGrades = listOf(
			"A+", "A0", "B+", "C+", "C0", "D+", "D0", "F", "NP", "P"
		)
	}
}

@JvmInline
value class CourseType(
	val value: String
) {
	val isMajor: Boolean
		get() = value.first() == '전'

	val isCulture: Boolean
		get() = value.first() == '교'

	val isGeneral: Boolean
		get() = value.first() == '일'

	val isSecondMajor: Boolean
		get() = value.first() == '복'

	val isMinor: Boolean
		get() = value.first() == '부'

	val isBasic: Boolean
		get() = value.first() == '기'

	val isRequired: Boolean
		get() = value.last() == '필'

	val isElective: Boolean
		get() = value.last() == '선'
}

data class SubjectGrade(
	@SerializedName("certname")
	val certName: String?,
	@SerializedName("gwamokKname")
	val subjectName: String,
	@SerializedName("hakgwa")
	val department: String,
	@SerializedName("hakjungNo")
	val academicNumber: String,
	@SerializedName("codeName1")
	val course: CourseType,
	@SerializedName("hakjumNum")
	val credits: Int,
	@SerializedName("retakeOpt")
	@JsonAdapter(YNBoolDeserializer::class)
	val retake: Boolean,
	@SerializedName("retakeGetGrade")
	val retakeGrade: String?,
	@SerializedName("getGrade")
	val grade: Grade
)
