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

import java.time.ZonedDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.openklas.klas.deserializer.ZonedDateTimeSerializer

@Serializable
data class Attachment(
	@SerialName("attachId")
	val id: String,
	@SerialName("createdAt")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val creationDate: ZonedDateTime,
	@SerialName("download")
	val url: String, // relative url may be provided
	@SerialName("ext")
	val extension: String,
	@SerialName("fileName")
	val fileName: String,
	@SerialName("fileSize")
	val fileSize: Long,
	// fileSn? probably related to the order
	@SerialName("id")
	val order: Int,
	// preview, thumbnail, saveName is unknown
	@SerialName("storageId")
	val storageId: String
)
