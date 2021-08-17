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
data class Assignment(
	@SerialName("rpt")
	val description: Description
) {
	@Serializable
	data class Description(
		@SerialName("atchFileId")
		val attachmentId: String?,
		@SerialName("contents") // why plural??
		val content: String,
		@SerialName("expiredate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val due: ZonedDateTime,
		@SerialName("filelimit")
		val submitFileSizeLimitMB: Int,
		@SerialName("indate")
		@Serializable(with = YNBoolSerializer::class)
		val isInPeriod: Boolean,
		@SerialName("reexpiredate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val extendedDue: ZonedDateTime?,
		@SerialName("startdate")
		@Serializable(with = ZonedDateTimeSerializer::class)
		val startDate: ZonedDateTime,
		@SerialName("submitfiletype")
		val submitFileExtensions: String,
		@SerialName("submityn")
		@Serializable(with = YNBoolSerializer::class)
		val isSubmitted: Boolean,
		@SerialName("title")
		val title: String,
		@SerialName("weeklyseq")
		val week: Int,
		@SerialName("weeklysubseq")
		val nthOfWeek: Int
	)
}
