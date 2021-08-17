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
import me.onebone.openklas.klas.deserializer.YNBoolSerializer
import me.onebone.openklas.klas.deserializer.ZonedDateTimeSerializer

@Serializable
data class Board(
	@SerialName("list")
	val posts: List<Entry>,
	@SerialName("page")
	val pageInfo: PageInfo
) {
	@Serializable
	data class PageInfo(
		@SerialName("currentPage")
		val currentPage: Int,
		@SerialName("pageSize")
		val postsPerPage: Int,
		@SerialName("totalElements")
		val totalPosts: Int,
		@SerialName("totalPages")
		// maximum page that can be requested is totalPages-1
		val totalPages: Int
	)

	@Serializable
	// unknown fields are written as comments
	data class Entry(
		@SerialName("atchFileId")
		val attachment: String?,
		val boardNo: Int,
		@SerialName("bunban")
		val division: String,
		// categoryId
		// categoryNm
		// cmCnt
		@SerialName("dateDiff")
		val dayFromUploaded: Int,
		@SerialName("fileCnt")
		val fileCnt: Int,
		val grcode: String,
		@SerialName("hakgi")
		val term: Int,
		@SerialName("masterNo")
		val masterNo: Int,
		@SerialName("myarticleAt")
		@Serializable(with = YNBoolSerializer::class)
		val isMine: Boolean,
		@SerialName("othbcAt")
		@Serializable(with = YNBoolSerializer::class)
		val isPublic: Boolean,
		@SerialName("readCnt")
		val hits: Int,
		@SerialName("refLvl")
		val referenceLevel: Int,
		// refSort
		@SerialName("registDt")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val registerDate: ZonedDateTime,
		val registerId: String,
		// rnum
		@SerialName("sortOrdr")
		val order: Int,
		@SerialName("subj")
		val subjectId: String,
		@SerialName("title")
		val title: String,
		@SerialName("topAt")
		@Serializable(with = YNBoolSerializer::class)
		val isPinned: Boolean,
		@SerialName("userNm")
		val author: String,
		// upperNo: same with boardNo?
		@SerialName("year")
		val year: Int
	)
}
