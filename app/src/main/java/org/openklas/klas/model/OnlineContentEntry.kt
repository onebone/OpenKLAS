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

// classified by "evltnSe" field
sealed class OnlineContentEntry {
	abstract val evltnSe: String

	data class Video(
		override val evltnSe: String, // lesson
		@SerializedName("bunban")
		val division: String,
		@SerializedName("sbjt")
		val title: String,
		@SerializedName("registDt")
		val registerDate: Date,
		@SerializedName("startDate")
		val startDate: Date,
		@SerializedName("endDate")
		val endDate: Date,
		@SerializedName("prog")
		val progress: Int,
		@SerializedName("rcognTime")
		val lectureTime: Int,
		@SerializedName("achivTime")
		val acquiredTime: Int,
		@SerializedName("starting")
		val uri: String,
	): OnlineContentEntry()

	data class Homework(
		override val evltnSe: String, // proj
		@SerializedName("registDt")
		val submitDate: Date?,
		@SerializedName("startDate")
		val startDate: Date,
		@SerializedName("endDate")
		val endDate: Date,
		@SerializedName("title")
		val title: String,
		@SerializedName("rcognTime")
		val lectureTime: Int,
		@SerializedName("achivTime")
		val acquiredTime: Int
	): OnlineContentEntry()

	data class Dummy(
		override val evltnSe: String
	): OnlineContentEntry()

	// TODO add more entry types
}
