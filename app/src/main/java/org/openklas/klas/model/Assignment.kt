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

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.openklas.klas.deserializer.YNBoolDeserializer
import java.util.Date

data class Assignment(
	@SerializedName("rpt")
	val description: Description
) {
	data class Description(
		@SerializedName("atchFileId")
		val attachmentId: String,
		@SerializedName("expiredate")
		val due: Date,
		@SerializedName("filelimit")
		val submitFileSizeLimitMB: Int,
		@SerializedName("indate")
		@JsonAdapter(YNBoolDeserializer::class)
		val isInPeriod: Boolean,
		@SerializedName("reexpiredate")
		val extendedDue: Date?,
		@SerializedName("startdate")
		val startDate: Date,
		@SerializedName("submitfiletype")
		val submitFileExtensions: String,
		@SerializedName("submityn")
		@JsonAdapter(YNBoolDeserializer::class)
		val isSubmitted: Boolean,
		@SerializedName("title")
		val title: String,
		@SerializedName("weeklyseq")
		val week: Int,
		@SerializedName("weeklysubseq")
		val nthOfWeek: Int
	)
}
