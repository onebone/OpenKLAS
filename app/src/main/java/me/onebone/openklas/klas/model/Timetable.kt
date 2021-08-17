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

import kotlinx.serialization.Serializable
import me.onebone.openklas.klas.deserializer.TimetableSerializer

@Serializable(with = TimetableSerializer::class)
data class Timetable(
	val entries: List<Entry>
) {
	@Serializable
	data class Entry (
		val day: Int,
		// wtTime
		val time: Int,
		// wtLocHname_%d
		val classroom: String,
		// wtProfNm_%d
		val professor: String?,
		// wtSpan_%d
		val length: Int,
		// wtSubjNm_%d
		val subjectName: String,
		// wtSubj_%d
		val subjectId: String,
		// wtYearhakgi_%d
		val semester: String,
		// wtSubjPrintSeq_%d
		// cannot figure out what exactly this is
		// but looks like it is only used to select
		// item color in timetable
		val printSeq: Int
	)
}
