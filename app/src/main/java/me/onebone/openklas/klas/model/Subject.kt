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
import me.onebone.openklas.klas.deserializer.YNBoolSerializer

@Serializable
data class Subject(
	@SerialName("subj")
	val id: String,
	@SerialName("bunban")
	val division: String,
	// codenmord
	// grcode
	@SerialName("hakjungno")
	val academicNumber: String,
	@SerialName("profNm")
	val professor: String,
	@SerialName("subjNm")
	val name: String,
	@SerialName("year")
	val year: Int,
	@SerialName("hakgi")
	val term: Int,
	@SerialName("yearhakgi")
	val semester: String,
	@SerialName("openOrganCodeNm")
	val targetDegree: String,
	@SerialName("iselearn")
	@Serializable(with = YNBoolSerializer::class)
	val isELearning: Boolean
)
