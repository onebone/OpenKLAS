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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.openklas.klas.deserializer.YNBoolSerializer

@Serializable
data class SemesterGrade(
	@SerialName("hakgi")
	val term: Int,
	@SerialName("thisYear")
	val year: Int,
	@SerialName("hakgiOrder")
	val semesterLabel: String,
	@SerialName("sungjukList")
	val grades: List<SubjectGrade>
)

@JvmInline
@Serializable
value class Grade(
	private val value: String
) {
	val hasSettled: Boolean
		get() = value in AvailableGrades

	val grade: String?
		get() = AvailableGrades.find { value.startsWith(it) }

	val point: Double?
		get() = PointMap[grade]

	val isGpaCounted: Boolean
		get() = grade in PointMap

	companion object {
		private val PointMap = mapOf(
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
@Serializable
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

@Serializable
data class SubjectGrade(
	@SerialName("certname")
	val certName: String?,
	@SerialName("gwamokKname")
	val subjectName: String,
	@SerialName("hakgwa")
	val department: String,
	@SerialName("hakjungNo")
	val academicNumber: String,
	@SerialName("codeName1")
	val course: CourseType,
	@SerialName("hakjumNum")
	val credits: Int,
	@SerialName("retakeOpt")
	@Serializable(with = YNBoolSerializer::class)
	val retake: Boolean,
	@SerialName("retakeGetGrade")
	val retakeGrade: String?,
	@SerialName("getGrade")
	val grade: Grade
)
