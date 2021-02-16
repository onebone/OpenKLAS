package org.openklas.klas.deserializer

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

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.openklas.klas.model.Book
import org.openklas.klas.model.Credits
import org.openklas.klas.model.Expectation
import org.openklas.klas.model.LectureMethod
import org.openklas.klas.model.LectureType
import org.openklas.klas.model.ScoreWeights
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.Tutor
import org.openklas.klas.model.VL
import org.openklas.klas.model.Week
import java.lang.reflect.Type

class SyllabusDeserializer: TypeResolvableJsonDeserializer<Syllabus> {
	override fun getType(): Type {
		return Syllabus::class.java
	}

	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Syllabus {
		val array = json.asJsonArray
		val obj = array[0].asJsonObject // why array??

		return Syllabus(
			subjectName = obj["gwamokKname"].asString,
			subjectNameEnglish = obj["gwamokEname"].asStringOrNull,
			departmentCode = obj["openMajorCode"].asString,
			targetGrade = obj["openGrade"].asInt,
			openGwamokNo = obj["openGwamokNo"].asString,
			division = obj["bunbanNo"].asString,
			course = obj["codeName1"].asString,
			introductionVideoUrl = obj["videoUrl"].asStringOrNull,
			credits = obj["hakjumNum"].asInt,
			lessonHours = obj["sisuNum"].asInt,
			tutor = deserializeTutor(obj),
			containsEnglish = obj["engOpt"].asStringOrNull == "1",
			englishPercentage = obj["englishBiyul"].asIntOrNull,
			containsSecondaryLanguage = obj["frnOpt"].asStringOrNull == "1",
			secondaryLanguagePercentage = obj["frnBiyul"].asIntOrNull,
			summary = obj["summary"].asString,
			purpose = obj["purpose"].asStringOrNull,
			expectations = deserializeExpectations(obj),
			creditDetails = deserializeCreditDetails(obj),
			recommendedPrerequisiteSubject = obj["preGwamok"].asStringOrNull,
			subsequentSubject = obj["postGwamok"].asStringOrNull,
			lectureType = deserializeLectureType(obj),
			lectureMethod = deserializeLectureMethod(obj),
			vlCompetence = deserializeVL(obj),
			scoreWeights = deserializeScoreWeights(obj),
			books = deserializeBooks(obj),
			bookComment = obj["bigo"].asStringOrNull,
			schedule = deserializeSchedule(obj)
		)
	}

	private fun deserializeTutor(json: JsonObject): Tutor {
		return Tutor(
			json["memberName"].asString,
			json["jikgeubName"].asString,
			json["telNo"].asStringOrNull,
			json["hpNo"].asStringOrNull,
			json["email"].asStringOrNull
		)
	}

	private fun deserializeExpectations(json: JsonObject): Array<Expectation> {
		val expectations = mutableListOf<Expectation>()

		for(i in 1..20) {
			if(!json["reflectPer$i"].isJsonNull) {
				expectations += Expectation(
					json["studyResultShort$i"].asString,
					json["result$i"].asStringOrNull
				)
			}
		}

		return expectations.toTypedArray()
	}

	private fun deserializeCreditDetails(json: JsonObject): Credits {
		return Credits(
			json["getScore1"].asIntOrNull ?: 0,
			json["getScore2"].asIntOrNull ?: 0,
			json["getScore3"].asIntOrNull ?: 0
		)
	}

	private fun deserializeLectureType(json: JsonObject): Int {
		val map = mapOf(
			"tblOpt" to LectureType.TBL,
			"pblOpt" to LectureType.PBL,
			"seminarOpt" to LectureType.SEMINAR,
			"typeSmall" to LectureType.SMALL,
			"typeFusion" to LectureType.FUSION,
			"typeTeam" to LectureType.TEAM_TEACHING,
			"typeWork" to LectureType.APPRENTICESHIP,
			"typeForeigner" to LectureType.FOREIGNER_ONLY,
			"typeElearn" to LectureType.E_LEARNING,
			"onlineOpt" to LectureType.ONLINE,
			"typeBlended" to LectureType.BLENDED,
			"typeExperiment" to LectureType.EXPERIMENT
		)

		return map.entries.fold(0) { acc, entry ->
			if(json[entry.key].asStringOrNull == "1") {
				acc or entry.value
			}else{
				acc
			}
		}
	}

	private fun deserializeLectureMethod(json: JsonObject): Int {
		val map = mapOf(
			"face100Opt" to LectureMethod.FACE_TO_FACE_100,
			"faceliveOpt" to LectureMethod.FACE_TELECONFERENCE_HALF,
			"live100Opt" to LectureMethod.TELECONFERENCE_100,
			"facerecOpt" to LectureMethod.FACE_TO_FACE_RECORD_HALF,
			"recliveOpt" to LectureMethod.RECORD_TELECONFERENCE,
			"rec100Opt" to LectureMethod.RECORD_100
		)

		for(entry in map.entries) {
			if(!json[entry.key].isJsonNull) return entry.value
		}

		return LectureMethod.NONE
	}

	private fun deserializeVL(json: JsonObject): VL {
		return VL(
			json["pa1"].asIntOrNull ?: 0,
			json["pa2"].asIntOrNull ?: 0,
			json["pa3"].asIntOrNull ?: 0,
			json["pa4"].asIntOrNull ?: 0,
			json["pa5"].asIntOrNull ?: 0,
			json["pa6"].asIntOrNull ?: 0,
			json["pa7"].asIntOrNull ?: 0,
		)
	}

	private fun deserializeScoreWeights(json: JsonObject): ScoreWeights {
		return ScoreWeights(
			json["attendBiyul"].asIntOrNull ?: 0,
			json["middleBiyul"].asIntOrNull ?: 0,
			json["lastBiyul"].asIntOrNull ?: 0,
			json["reportBiyul"].asIntOrNull ?: 0,
			json["learnBiyul"].asIntOrNull ?: 0,
			json["quizBiyul"].asIntOrNull ?: 0,
			json["gitaBiyul"].asIntOrNull ?: 0,
		)
	}

	private fun deserializeBooks(json: JsonObject): Array<Book> {
		val books = mutableListOf<Book>()

		for(i in 0..3) {
			val index = if(i == 0) "" else "$i"

			if(!json["book${index}Name"].isJsonNull) {
				books += Book(
					json["book${index}Name"].asString,
					json["write${index}Name"].asStringOrNull,
					json["company${index}Name"].asStringOrNull,
					json["print${index}Year"].asIntOrNull
				)
			}
		}

		return books.toTypedArray()
	}

	private fun deserializeSchedule(json: JsonObject): Array<Week> {
		val weeks = mutableListOf<Week>()

		for(i in 1..15) {
			if(!json["week${i}Lecture"].isJsonNull) {
				weeks += Week(
					i,
					json["week${i}Lecture"].asString,
					json["week${i}Bigo"].asStringOrNull,
					json["week${i}Subs"].asStringOrNull
				)
			}
		}

		return weeks.toTypedArray()
	}
}
