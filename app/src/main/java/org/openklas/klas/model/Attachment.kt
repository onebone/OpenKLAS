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
import java.time.OffsetDateTime

data class Attachment(
	@SerializedName("attachId")
	val id: String,
	@SerializedName("createdAt")
	val creationDate: OffsetDateTime,
	@SerializedName("download")
	val url: String, // relative url may be provided
	@SerializedName("ext")
	val extension: String,
	@SerializedName("fileName")
	val fileName: String,
	@SerializedName("fileSize")
	val fileSize: Long,
	// fileSn? probably related to the order
	@SerializedName("id")
	val order: Int,
	// preview, thumbnail, saveName is unknown
	@SerializedName("storageId")
	val storageId: String
)
