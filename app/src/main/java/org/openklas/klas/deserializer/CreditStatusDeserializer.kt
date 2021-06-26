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

package org.openklas.klas.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import org.openklas.klas.model.CreditStatus
import java.lang.reflect.Type

class CreditStatusDeserializer: TypeResolvableJsonDeserializer<CreditStatus> {
	override fun getType(): Type {
		return CreditStatus::class.java
	}

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): CreditStatus {
		val obj = json.asJsonObject

		return CreditStatus(
			applied = CreditStatus.Credits(
				major = obj["majorApplyHakjum"].asInt,
				majorRetake = obj["retakeMajorApplyHakjum"].asInt,
				elective = obj["cultureApplyHakjum"].asInt,
				electiveRetake = obj["retakeCultureApplyHakjum"].asInt,
				others = obj["etcApplyHakjum"].asInt,
				othersRetake = obj["retakeEtcApplyHakjum"].asInt,
				sum = obj["applyHakjum"].asInt,
				sumRetake = obj["retakeApplyHakjum"].asInt
			),
			deleted = CreditStatus.Credits(
				major = obj["majorDelHakjum"].asInt,
				majorRetake = obj["retakeMajorDelHakjum"].asInt,
				elective = obj["cultureDelHakjum"].asInt,
				electiveRetake = obj["retakeCultureDelHakjum"].asInt,
				others = obj["etcDelHakjum"].asInt,
				othersRetake = obj["retakeEtcDelHakjum"].asInt,
				sum = obj["delHakjum"].asInt,
				sumRetake = obj["retakeDelHakjum"].asInt
			),
			acquired = CreditStatus.Credits(
				major = obj["majorChidukHakjum"].asInt,
				majorRetake = obj["retakeMajorDelHakjum"].asInt,
				elective = obj["cultureChidukHakjum"].asInt,
				electiveRetake = obj["retakeCultureDelHakjum"].asInt,
				others = obj["etcChidukHakjum"].asInt,
				othersRetake = obj["retakeEtcDelHakjum"].asInt,
				sum = obj["chidukHakjum"].asInt,
				sumRetake = obj["retakeDelHakjum"].asInt
			)
		)
	}
}
