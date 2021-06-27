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

data class Board(
	@SerializedName("list")
	val posts: Array<Entry>,
	@SerializedName("page")
	val pageInfo: PageInfo
) {
	data class PageInfo(
		val currentPage: Int,
		@SerializedName("pageSize")
		val postsPerPage: Int,
		@SerializedName("totalElements")
		val totalPosts: Int,
		@SerializedName("totalPages")
		// maximum page that can be requested is totalPages-1
		val totalPages: Int
	)

	// unknown fields are written as comments
	data class Entry(
		@SerializedName("atchFileId")
		val attachment: String?,
		val boardNo: Int,
		@SerializedName("bunban")
		val division: String,
		// categoryId
		// categoryNm
		// cmCnt
		@SerializedName("dateDiff")
		val dayFromUploaded: Int,
		@SerializedName("fileCnt")
		val fileCnt: Int,
		val grcode: String,
		@SerializedName("hakgi")
		val term: Int,
		@SerializedName("masterNo")
		val masterNo: Int,
		@SerializedName("myarticleAt")
		val isMine: Boolean,
		@SerializedName("othbcAt")
		val isPublic: Boolean,
		@SerializedName("readCnt")
		val hits: Int,
		@SerializedName("refLvl")
		val referenceLevel: Int,
		// refSort
		val registeredAt: ZonedDateTime,
		val registerId: String,
		// rnum
		@SerializedName("sortOrdr")
		val order: Int,
		@SerializedName("subj")
		val subjectId: String,
		@SerializedName("title")
		val title: String,
		@SerializedName("topAt")
		val isPinned: Boolean,
		@SerializedName("userNm")
		val author: String,
		// upperNo: same with boardNo?
		@SerializedName("year")
		val year: Int
	)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Board) return false

		if (!posts.contentEquals(other.posts)) return false
		if (pageInfo != other.pageInfo) return false

		return true
	}

	override fun hashCode(): Int {
		var result = posts.contentHashCode()
		result = 31 * result + pageInfo.hashCode()
		return result
	}
}
