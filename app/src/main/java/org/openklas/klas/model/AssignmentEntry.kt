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

data class AssignmentEntry(
	@SerializedName("adddate")
	@JsonAdapter(YNBoolDeserializer::class)
	val isExtendedPeriod: Boolean,
	@SerializedName("expiredate")
	val due: Date,
	// [isSubmitPeriod] is a flag indicating if a user can
	// submit a homework now. Returns true if current time is
	// after [startDate] and before [due].
	@SerializedName("indate")
	@JsonAdapter(YNBoolDeserializer::class)
	val isSubmitPeriod: Boolean,
	@SerializedName("ordseq")
	val order: Int,
	@SerializedName("reexpiredate")
	val extendedDue: Date?,
	@SerializedName("restartdate")
	val extendedStartDate: Date?,
	@SerializedName("score")
	val score: Int?,
	@SerializedName("startdate")
	val startDate: Date,
	@SerializedName("submityn")
	@JsonAdapter(YNBoolDeserializer::class)
	val isSubmitted: Boolean,
	@SerializedName("taskNo")
	val taskNumber: Int,
	@SerializedName("title")
	val title: String,
	@SerializedName("weeklyseq")
	val week: Int,
	@SerializedName("weeklysubseq")
	val nthOfWeek: Int,
	// @SerializedName("weight"): unknown
)
