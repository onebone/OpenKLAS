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

data class PostComposite(
	@SerializedName("board")
	val post: Post,
	@SerializedName("boardPre")
	val previous: Preview?,
	@SerializedName("boardNex")
	val next: Preview?,
	@SerializedName("comment")
	val comments: Array<Comment>?
) {
	data class Post(
		@SerializedName("atchFileId")
		val attachmentId: String?,
		@SerializedName("boardNo")
		val boardNo: Int,
		@SerializedName("content")
		val content: String,
		@SerializedName("masterNo")
		val masterNo: Int,
		@SerializedName("readCnt")
		val hits: Int,
		@SerializedName("refLvl")
		val depth: Int,
		@SerializedName("refSort")
		val orderInCurrentDepth: Int,
		@SerializedName("registDt")
		val registerDate: ZonedDateTime,
		@SerializedName("registerId")
		val authorId: String,
		@SerializedName("registerInfo")
		val authorInfo: String,
		@SerializedName("registerIp")
		val authorIp: String,
		@SerializedName("sortOrdr")
		val order: Int,
		@SerializedName("title")
		val title: String,
		@SerializedName("topAt")
		val isPinned: Boolean,
		@SerializedName("updtDt")
		val updateDate: ZonedDateTime,
		@SerializedName("updusrId")
		val updateAuthorId: String?,
		@SerializedName("updusrInfo")
		val updateAuthorInfo: String?,
		@SerializedName("updusrIp")
		val updateAuthorIp: String?,
		// upperNo??
		@SerializedName("userNm")
		val author: String
	)

	data class Preview(
		@SerializedName("boardNo")
		val boardNo: Int,
		@SerializedName("masterNo")
		val masterNo: Int,
		@SerializedName("sortOrdr")
		val order: Int,
		@SerializedName("title")
		val title: String
	)

	data class Comment(
		@SerializedName("boardNo")
		val boardNo: Int,
		@SerializedName("cm")
		val content: String,
		@SerializedName("cmNo")
		val id: String,
		@SerializedName("loginId")
		val authorLoginId: String,
		@SerializedName("mtype")
		val mtype: String, // "P", "S": Professor, Student??
		@SerializedName("myarticleAt")
		val isMine: Boolean,
		@SerializedName("registDt")
		val registerDate: Date,
		@SerializedName("registerId")
		val authorId: String,
		@SerializedName("userNm")
		val author: String
	)

	override fun equals(other: Any?): Boolean {
		if(this === other) return true
		if(other !is PostComposite) return false

		if(post != other.post) return false
		if(previous != other.previous) return false
		if(next != other.next) return false
		if(comments != null) {
			if(other.comments == null) return false
			if(!comments.contentEquals(other.comments)) return false
		}else if(other.comments != null) return false

		return true
	}

	override fun hashCode(): Int {
		var result = post.hashCode()
		result = 31 * result + (previous?.hashCode() ?: 0)
		result = 31 * result + (next?.hashCode() ?: 0)
		result = 31 * result + (comments?.contentHashCode() ?: 0)
		return result
	}
}

enum class PostType {
	NOTICE, LECTURE_MATERIAL, QNA
}
