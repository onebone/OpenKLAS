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

import java.time.ZonedDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.openklas.klas.deserializer.ZonedDateTimeSerializer

@Serializable
data class BriefNotice (
	@SerialName("subj")
	val subjectId: String,
	@SerialName("bunban")
	val division: String,
	// grcode?
	val param: String,
	@SerialName("year")
	val year: Int,
	@SerialName("hakgi")
	val term: Int,
	@SerialName("registDt")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val postDate: ZonedDateTime,
	@SerialName("subjNm")
	val subjectName: String,
	@SerialName("title")
	val title: String,
	val type: Int,
	@SerialName("typeNm")
	val typeName: String,
	@SerialName("yearhakgi")
	val semester: String
)
