package org.openklas.klas.model

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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

data class Semester (
	@SerializedName("value")
	val id: String,
	val label: String,
	@SerializedName("subjList")
	val subjects: Array<BriefSubject>
) {
	override fun equals(other: Any?): Boolean {
		if(this === other) return true
		if(other !is Semester) return false

		if(id != other.id) return false
		if(label != other.label) return false
		if(!subjects.contentEquals(other.subjects)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + label.hashCode()
		result = 31 * result + subjects.contentHashCode()
		return result
	}
}
