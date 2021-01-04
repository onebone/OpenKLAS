package org.openklas.klas.model

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

import com.google.gson.annotations.SerializedName

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
) {
	override fun equals(other: Any?): Boolean {
		if(this === other) return true
		if(other !is Home) return false

		if(!subjects.contentEquals(other.subjects)) return false
		if(professor != other.professor) return false
		if(!notices.contentEquals(other.notices)) return false
		if(semesterLabel != other.semesterLabel) return false
		if(timetable != other.timetable) return false

		return true
	}

	override fun hashCode(): Int {
		var result = subjects.contentHashCode()
		result = 31 * result + professor.hashCode()
		result = 31 * result + notices.contentHashCode()
		result = 31 * result + semesterLabel.hashCode()
		result = 31 * result + timetable.hashCode()
		return result
	}
}
