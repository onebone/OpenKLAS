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
data class SchoolRegister(
	@SerialName("cultureHakjum")
	val electiveCredits: Int,
	@SerialName("majorHakjum")
	val majorCredits: Int,
	@SerialName("hakbun")
	val studentId: String,
	@SerialName("hakgwa")
	val department: String,
	@SerialName("kname")
	val studentName: String,
	@SerialName("hakgwaCode")
	val departmentCode: String,
	@SerialName("hakjukStatu")
	val registerStatus: String,
	@SerialName("grade")
	val grade: Int
)
