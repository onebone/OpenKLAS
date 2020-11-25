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

data class Subject (
	@SerializedName("bunban")
	val division: String,
	// codenmord
	// grcode
	@SerializedName("hakgi")
	val term: String,
	@SerializedName("hakjungno")
	val academicNumber: String,
	@SerializedName("profNm")
	val professor: String,
	@SerializedName("subj")
	val id: String,
	@SerializedName("subjNm")
	val name: String,
	@SerializedName("year")
	val year: Int,
	@SerializedName("yearhakgi")
	val semester: String,
	@SerializedName("openOrganCodeNm")
	val targetDegree: String,
	@SerializedName("iselearn")
	val isELearning: Boolean
)
