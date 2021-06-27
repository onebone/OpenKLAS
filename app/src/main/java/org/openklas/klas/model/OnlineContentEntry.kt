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

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime
import java.util.Date

// classified by "evltnSe" field
sealed class OnlineContentEntry {
	abstract val evltnSe: String

	abstract val title: String
	abstract val startDate: ZonedDateTime
	abstract val dueDate: ZonedDateTime

	data class Video(
		override val evltnSe: String, // lesson
		@SerializedName("bunban")
		val division: String,
		@SerializedName("sbjt")
		override val title: String,
		@SerializedName("registDt")
		val registerDate: ZonedDateTime,
		@SerializedName("startDate")
		override val startDate: ZonedDateTime,
		@SerializedName("endDate")
		override val dueDate: ZonedDateTime,
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
		override val startDate: ZonedDateTime,
		@SerializedName("endDate")
		override val dueDate: ZonedDateTime,
		@SerializedName("sbjt")
		override val title: String,
		@SerializedName("rcognTime")
		val lectureTime: Int,
		@SerializedName("achivTime")
		val acquiredTime: Int
	): OnlineContentEntry()

	data class Quiz(
		override val evltnSe: String, // quiz
		@SerializedName("endDate")
		override val dueDate: ZonedDateTime,
		@SerializedName("achivTime")
		val acquiredTime: Int,
		@SerializedName("lrnPd")
		val takenFromTo: String,
		@SerializedName("module")
		val chapter: String, // 01, 02, ...
		@SerializedName("moduletitle")
		val chapterTitle: String,
		@SerializedName("sbjt")
		override val title: String,
		@SerializedName("rcognTime")
		// Note that [lectureTime] is different from the actual
		// length of the lecture
		val lectureTime: Int,
		@SerializedName("startDate")
		override val startDate: ZonedDateTime,
		@SerializedName("started")
		val takenAt: Date?,
		@SerializedName("userId")
		val userId: String,
		@SerializedName("weekNo")
		val week: Int,
		@SerializedName("papernum")
		val id: Int,
		@SerializedName("papernm")
		val name: String
	): OnlineContentEntry()

	data class Dummy(
		override val evltnSe: String,
		@SerializedName("sbjt")
		override val title: String,
		@SerializedName("endDate")
		override val dueDate: ZonedDateTime,
		@SerializedName("startDate")
		override val startDate: ZonedDateTime
	): OnlineContentEntry()

	// TODO add more entry types
}
