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
	val semester: Int,
	@SerializedName("thisYear")
	val year: Int,
	@SerializedName("hakgiOrder")
	val semesterLabel: String,
	@SerializedName("sungjukList")
	val grades: List<Grade>
)

data class Grade(
	@SerializedName("certname")
	val certName: String?,
	@SerializedName("gwamokKname")
	val subjectName: String,
	@SerializedName("hakgwa")
	val department: String,
	@SerializedName("hakjungNo")
	val academicNumber: String,
	@SerializedName("hakjumNum")
	val credits: Int,
	@SerializedName("retakeOpt")
	@JsonAdapter(YNBoolDeserializer::class)
	val retake: Boolean,
	@SerializedName("retakeGetGrade")
	val retakeGrade: String?,
	@SerializedName("getGrade")
	val grade: String
)