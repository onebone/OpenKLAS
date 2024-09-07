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

import java.time.ZonedDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.onebone.openklas.klas.deserializer.ForceIntSerializer
import me.onebone.openklas.klas.deserializer.OnlineContentEntrySerializer
import me.onebone.openklas.klas.deserializer.ZonedDateTimeSerializer

// classified by "evltnSe" field
@Serializable(with = OnlineContentEntrySerializer::class)
sealed class OnlineContentEntry {
	abstract val evltnSe: String

	abstract val title: String
	abstract val startDate: ZonedDateTime
	abstract val dueDate: ZonedDateTime

	@Serializable
	data class Video(
		override val evltnSe: String, // lesson
		@SerialName("bunban")
		val division: String,
		@SerialName("sbjt")
		override val title: String,
		@SerialName("startDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val startDate: ZonedDateTime,
		@SerialName("endDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val dueDate: ZonedDateTime,
		@SerialName("prog")
		val progress: Float,
		@Serializable(with = ForceIntSerializer::class)
		@SerialName("rcognTime")
		val lectureTime: Int,
		@Serializable(with = ForceIntSerializer::class)
		@SerialName("achivTime")
		val acquiredTime: Int,
		@SerialName("starting")
		val uri: String?, // No "play" button when `null`
	): OnlineContentEntry()

	@Serializable
	data class Homework(
		override val evltnSe: String, // proj
		@SerialName("registDt")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val submitDate: ZonedDateTime?,
		@SerialName("startDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val startDate: ZonedDateTime,
		@SerialName("endDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val dueDate: ZonedDateTime,
		@SerialName("sbjt")
		override val title: String,
		@SerialName("rcognTime")
		@Serializable(with = ForceIntSerializer::class)
		val lectureTime: Int,
		@SerialName("achivTime")
		@Serializable(with = ForceIntSerializer::class)
		val acquiredTime: Int
	): OnlineContentEntry()

	@Serializable
	data class Quiz(
		override val evltnSe: String, // quiz
		@SerialName("endDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val dueDate: ZonedDateTime,
		@SerialName("achivTime")
		val acquiredTime: Int,
		@SerialName("lrnPd")
		val takenFromTo: String,
		@SerialName("module")
		val chapter: String, // 01, 02, ...
		@SerialName("moduletitle")
		val chapterTitle: String,
		@SerialName("sbjt")
		override val title: String,
		@Serializable(with = ForceIntSerializer::class)
		@SerialName("rcognTime")
		// Note that [lectureTime] is different from the actual
		// length of the lecture
		val lectureTime: Int,
		@SerialName("startDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val startDate: ZonedDateTime,
		@SerialName("started")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val takenAt: ZonedDateTime?,
		@SerialName("userId")
		val userId: String,
		@SerialName("weekNo")
		val week: Int,
		@SerialName("papernum")
		val id: Int,
		@SerialName("papernm")
		val name: String
	): OnlineContentEntry()

	@Serializable
	data class Dummy(
		override val evltnSe: String,
		@SerialName("sbjt")
		override val title: String,
		@SerialName("endDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val dueDate: ZonedDateTime,
		@SerialName("startDate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		override val startDate: ZonedDateTime
	): OnlineContentEntry()

	// TODO add more entry types
}
