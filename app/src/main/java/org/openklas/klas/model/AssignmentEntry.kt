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
import org.openklas.klas.deserializer.YNBoolSerializer
import org.openklas.klas.deserializer.ZonedDateTimeSerializer

@Serializable
data class AssignmentEntry(
	@SerialName("adddate")
	@Serializable(with = YNBoolSerializer::class)
	val isExtendedPeriod: Boolean,
	@SerialName("expiredate")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val due: ZonedDateTime,
	// [isSubmitPeriod] is a flag indicating if a user can
	// submit a homework now. Returns true if current time is
	// after [startDate] and before [due].
	@SerialName("indate")
	@Serializable(with = YNBoolSerializer::class)
	val isSubmitPeriod: Boolean,
	@SerialName("ordseq")
	val order: Int,
	@SerialName("reexpiredate")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val extendedDue: ZonedDateTime?,
	@SerialName("restartdate")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val extendedStartDate: ZonedDateTime?,
	@SerialName("score")
	val score: Int?,
	@SerialName("startdate")
	@Serializable(with = ZonedDateTimeSerializer::class)
	val startDate: ZonedDateTime,
	@SerialName("submityn")
	@Serializable(with = YNBoolSerializer::class)
	val isSubmitted: Boolean,
	@SerialName("taskNo")
	val taskNumber: Int,
	@SerialName("title")
	val title: String,
	@SerialName("weeklyseq")
	val week: Int,
	@SerialName("weeklysubseq")
	val nthOfWeek: Int,
	// @SerialName("weight"): unknown
)
