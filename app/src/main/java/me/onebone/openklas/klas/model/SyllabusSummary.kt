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

package me.onebone.openklas.klas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyllabusSummary(
	@SerialName("bunbanNo")
	val division: String,
	// closeOpt: whether the lecture is cancelled
	// Available field values are: 'Y', null?????????
	@SerialName("codeName1")
	val course: String,
	@SerialName("gwamokKname")
	val subjectName: String,
	@SerialName("hakgi")
	val term: Int,
	@SerialName("thisYear")
	val year: Int,
	@SerialName("memberName")
	val tutor: String?,
	@SerialName("openGrade")
	val targetGrade: Int,
	@SerialName("openGwamokNo")
	val openGwamokNo: String,
	@SerialName("openMajorCode")
	val departmentCode: String,
	@SerialName("hakjumNum")
	val credits: Int,
	@SerialName("sisuNum")
	val lessonHour: Int,
	@SerialName("summary")
	val summary: String?, // if summary is null, syllabus is not ready
	@SerialName("telNo")
	val telephoneNumber: String?,
	@SerialName("videoUrl")
	val videoUrl: String?
) {
	val isReady: Boolean
		get() = summary != null
}
