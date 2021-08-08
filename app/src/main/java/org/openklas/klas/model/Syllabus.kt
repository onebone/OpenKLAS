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

data class Tutor(
	// memberName
	val name: String,
	// jikgeubName
	val type: String,
	// telNo
	val telephoneContact: String?,
	// hpNo
	val contact: String?,
	// email
	val email: String?,
)

data class Expectation(
	// studyResultShort{d}
	val short: String,
	// result{d}
	val detail: String?
)

data class Credits(
	// getScore1
	val bookwork: Int,
	// getScore2
	val experiment: Int,
	// getScore3
	val design: Int
)

object LectureType {
	// value "1" is true
	// tblOpt
	const val TBL = 1
	// pblOpt
	const val PBL = 1 shl 1
	// seminarOpt
	const val SEMINAR = 1 shl 2
	// typeSmall
	const val SMALL = 1 shl 3
	// typeFusion
	const val FUSION = 1 shl 4
	// typeTeam
	const val TEAM_TEACHING = 1 shl 5
	// typeWork
	const val APPRENTICESHIP = 1 shl 6
	// typeForeigner
	const val FOREIGNER_ONLY = 1 shl 7
	// typeElearn
	const val E_LEARNING = 1 shl 8
	// onlineOpt
	const val ONLINE = 1 shl 9
	// typeBlended
	const val BLENDED = 1 shl 10
	// typeExperiment
	const val EXPERIMENT = 1 shl 11
}

object LectureMethod {
	const val NONE = -1
	// face100Opt
	const val FACE_TO_FACE_100 = 0
	// faceliveOpt
	const val FACE_TELECONFERENCE_HALF = 1
	// live100Opt
	const val TELECONFERENCE_100 = 2
	// facerecOpt
	const val FACE_TO_FACE_RECORD_HALF = 3
	// recliveOpt
	const val RECORD_TELECONFERENCE = 4
	// rec100Opt
	const val RECORD_100 = 5
}

data class VL(
	// pa1
	val profession: Int,
	// pa2
	val convergenceThinking: Int,
	// pa3
	val globalCompetence: Int,
	// pa4
	val socialCompetence: Int,
	// pa5
	val futureOriented: Int,
	// pa6
	val challengerMinded: Int,
	// pa7
	val sympathy: Int
)

data class ScoreWeights(
	// attendBiyul
	val attendance: Int,
	// middleBiyul
	val midterm: Int,
	// lastBiyul
	val finalTerm: Int,
	// reportBiyul
	val report: Int,
	// learnBiyul
	val attitude: Int,
	// quizBiyul
	val quiz: Int,
	// gitaBiyul
	val others: Int
)

data class Book(
	// book[1-3]?Name
	val name: String,
	// write[1-3]?Name
	val author: String?,
	// company[1-3]?Name
	val publisher: String?,
	// print[1-3]?Year
	val publicationYear: Int?
)

data class Week(
	val week: Int,
	// week[1-15]Lecture
	val content: String,
	// week[1-15]Bigo
	val note: String?,
	// week[1-15]Subs
	val supplementary: String?
)

// Note that required prerequisite must be fetched separately

// missing fields:
// recOptCheck(recVideo, recReport, recQuiz, recQna, recEtc)
// evaluationOpt
data class Syllabus(
	// gwamokKname
	val subjectName: String,
	// gwamokEname
	val subjectNameEnglish: String?,
	// openMajorCode
	val departmentCode: String,
	// openGrade
	val targetGrade: Int,
	// openGwamokNo
	val openGwamokNo: String,
	// bunbanNo
	val division: String,
	// codeName1
	val course: String,
	// videoUrl
	val introductionVideoUrl: String?,
	// hakjumNum
	val credits: Int,
	// sisuNum
	val lessonHours: Int,
	// memberName, jikgeubName, telNo
	val tutor: Tutor,
	// engOptCheck: 1 -> true, else -> false
	val containsEnglish: Boolean,
	// englishBiyul
	val englishPercentage: Int?,
	// frnOptCheck: 1 -> true, else -> false
	val containsSecondaryLanguage: Boolean,
	// frnBiyul
	val secondaryLanguagePercentage: Int?,
	// summary
	val summary: String,
	// purpose
	val purpose: String?,
	// reflectPer, studyResultShort, result (1~20)
	val expectations: List<Expectation>,
	// getScore[1-3]
	val creditDetails: Credits,
	// preGwamok
	val recommendedPrerequisiteSubject: String?,
	// postGwamok
	val subsequentSubject: String?,
	val lectureType: Int, // LectureType
	val lectureMethod: Int, // LectureMethod
	val vlCompetence: VL,
	val scoreWeights: ScoreWeights,
	val books: List<Book>,
	// bigo
	val bookComment: String?,
	val schedule: List<Week>
)
