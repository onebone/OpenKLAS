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

import java.time.LocalDate
import java.time.ZonedDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.onebone.openklas.klas.deserializer.LocalDateSerializer
import me.onebone.openklas.klas.deserializer.YNBoolSerializer
import me.onebone.openklas.klas.deserializer.ZonedDateTimeSerializer

@Serializable
data class PostComposite(
	@SerialName("board")
	val post: Post,
	// QnA board does not set previous and next board for us
	@SerialName("boardPre")
	val previous: Preview? = null,
	@SerialName("boardNex")
	val next: Preview? = null,
	@SerialName("comment")
	val comments: List<Comment>? = null
) {
	@Serializable
	data class Post(
		@SerialName("atchFileId")
		val attachmentId: String?,
		@SerialName("boardNo")
		val boardNo: Int,
		@SerialName("content")
		val content: String,
		@SerialName("masterNo")
		val masterNo: Int,
		@SerialName("readCnt")
		val hits: Int,
		@SerialName("refLvl")
		val depth: Int,
		@SerialName("refSort")
		val orderInCurrentDepth: Int,
		@SerialName("registDt")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val registerDate: ZonedDateTime,
		@SerialName("registerId")
		val authorId: String,
		@SerialName("registerInfo")
		val authorInfo: String,
		@SerialName("registerIp")
		val authorIp: String,
		@SerialName("sortOrdr")
		val order: Int,
		@SerialName("title")
		val title: String,
		@SerialName("topAt")
		@Serializable(with = YNBoolSerializer::class)
		val isPinned: Boolean,
		@SerialName("updtDt")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val updateDate: ZonedDateTime?,
		@SerialName("updusrId")
		val updateAuthorId: String?,
		@SerialName("updusrInfo")
		val updateAuthorInfo: String?,
		@SerialName("updusrIp")
		val updateAuthorIp: String?,
		// upperNo??
		@SerialName("userNm")
		val author: String
	)

	@Serializable
	data class Preview(
		@SerialName("boardNo")
		val boardNo: Int,
		@SerialName("masterNo")
		val masterNo: Int,
		@SerialName("sortOrdr")
		val order: Int,
		@SerialName("title")
		val title: String
	)

	@Serializable
	data class Comment(
		@SerialName("boardNo")
		val boardNo: Int,
		@SerialName("cm")
		val content: String,
		@SerialName("cmNo")
		val id: Int,
		@SerialName("loginId")
		val authorLoginId: String,
		@SerialName("mtype")
		val mtype: String, // "P", "S": Professor, Student??
		@SerialName("myarticleAt")
		@Serializable(with = YNBoolSerializer::class)
		val isMine: Boolean,
		@SerialName("registDt")
		@Serializable(with = LocalDateSerializer::class)
		val registerDate: LocalDate, // comment does not give us specific time...
		@SerialName("registerId")
		val authorId: String,
		@SerialName("userNm")
		val author: String
	)
}

enum class PostType {
	NOTICE, LECTURE_MATERIAL, QNA
}
