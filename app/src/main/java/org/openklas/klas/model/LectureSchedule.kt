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

data class LectureSchedule(
	val day: Int,
	val dayLabel: String, // 일, 월, 화, ..., 토
	val classroom: String?,
	val periods: IntArray
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is LectureSchedule) return false

		if (day != other.day) return false
		if (dayLabel != other.dayLabel) return false
		if (classroom != other.classroom) return false
		if (!periods.contentEquals(other.periods)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = day
		result = 31 * result + dayLabel.hashCode()
		result = 31 * result + (classroom?.hashCode() ?: 0)
		result = 31 * result + periods.contentHashCode()
		return result
	}
}
