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
import java.util.Date

data class BriefNotice (
	@SerializedName("subj")
	val subjectId: String,
	@SerializedName("bunban")
	val division: String,
	// grcode?
	val param: String,
	@SerializedName("year")
	val year: Int,
	@SerializedName("hakgi")
	val term: Int,
	@SerializedName("registDt")
	val postDate: Date,
	@SerializedName("subjNm")
	val subjectName: String,
	@SerializedName("title")
	val title: String,
	val type: Int,
	@SerializedName("typeNm")
	val typeName: String,
	@SerializedName("yearhakgi")
	val semester: String
)
