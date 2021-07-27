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

import com.google.gson.annotations.SerializedName

data class SyllabusSummary(
	@SerializedName("bunbanNo")
	val division: String,
	// closeOpt: whether the lecture is cancelled
	// Available field values are: 'Y', null?????????
	@SerializedName("codeName1")
	val course: String,
	@SerializedName("gwamokKname")
	val subjectName: String,
	@SerializedName("hakgi")
	val term: Int,
	@SerializedName("thisYear")
	val year: Int,
	@SerializedName("memberName")
	val tutor: String,
	@SerializedName("openGrade")
	val targetGrade: Int,
	@SerializedName("openGwamokNo")
	val openGwamokNo: String,
	@SerializedName("openMajorCode")
	val departmentCode: String,
	@SerializedName("hakjumNum")
	val credits: Int,
	@SerializedName("sisuNum")
	val lessonHour: Int,
	@SerializedName("summary")
	val summary: String?, // if summary is null, syllabus is not ready
	@SerializedName("telNo")
	val telephoneNumber: String?,
	@SerializedName("videoUrl")
	val videoUrl: String?
) {
	val isReady: Boolean
		get() = summary != null
}
