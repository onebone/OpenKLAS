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

package org.openklas.klas.test

import kotlinx.coroutines.delay
import org.openklas.klas.KlasClient
import org.openklas.klas.error.KlasNoDataError
import org.openklas.klas.model.Assignment
import org.openklas.klas.model.AssignmentEntry
import org.openklas.klas.model.Attachment
import org.openklas.klas.model.Board
import org.openklas.klas.model.Book
import org.openklas.klas.model.BriefNotice
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.CreditStatus
import org.openklas.klas.model.Credits
import org.openklas.klas.model.Expectation
import org.openklas.klas.model.Home
import org.openklas.klas.model.LectureMethod
import org.openklas.klas.model.LectureSchedule
import org.openklas.klas.model.LectureType
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.PostComposite
import org.openklas.klas.model.Professor
import org.openklas.klas.model.SchoolRegister
import org.openklas.klas.model.ScoreWeights
import org.openklas.klas.model.Semester
import org.openklas.klas.model.SemesterGrade
import org.openklas.klas.model.Subject
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.model.TeachingAssistant
import org.openklas.klas.model.Timetable
import org.openklas.klas.model.Tutor
import org.openklas.klas.model.VL
import org.openklas.klas.model.Week
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.utils.Resource
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.random.Random

class DemoKlasClient @Inject constructor(): KlasClient {
	override suspend fun testSession(): Boolean {
		return true
	}

	override suspend fun login(username: String, password: String): Resource<String> {
		return Resource.Success(USER_ID)
	}

	override suspend fun getHome(semester: String): Resource<Home> {
		delay(NETWORK_DELAY)

		val printSeqs = (1..11).toMutableList()
		//var index = 0
		return Resource.Success(
			Home(
				subjects = SUBJECTS,
				professor = Professor("010-1234-5678", "", "", "", null),
				notices = NOTICES.values.flatMap { it.toList() }.toTypedArray(),
				semesterLabel = CURRENT_SEMESTER.label,
				timetable = Timetable(
					LECTURE_SCHEDULES.flatMap { (subjectId, schedules) ->
						val subject = findSubject(subjectId) ?: return@flatMap listOf()

						// FIXME using `index` variable here causes 'java.lang.IncompatibleClassChangeError'
						// should figure out if this is a bug of coroutine or just my mistake
						// val printSeq = index++
						val printSeq = printSeqs.random()
						printSeqs -= printSeq
						schedules.map {
							Timetable.Entry(it.day, it.periods.first(), it.classroom ?: CLASSROOM_UNDEFINED,
								subject.professor, it.periods.last() - it.periods.first() + 1, subject.name,
								subjectId, subject.semester, printSeq
							)
						}
					}
				)
			)
		)
	}

	override suspend fun getSemesters(): Resource<List<Semester>> {
		delay(NETWORK_DELAY)

		return Resource.Success(SEMESTERS)
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		delay(NETWORK_DELAY)

		val entries = NOTICES.entries.find { it.key == subjectId }?.value?.map {
			Board.Entry(null, -1, it.division, 0, 0, "01", it.term, 5,
				isMine = false, isPublic = true, Random.nextInt(0, 100), 0, ZonedDateTime.now(), "---", 0, it.subjectId,
				it.title, isPinned = false, "작성자", it.year
			)
		} ?: listOf()

		val pages = entries.chunked(15)
		val entriesInPage = if(page <= pages.lastIndex) pages[page] else listOf()

		return Resource.Success(Board(
			entriesInPage.toTypedArray(),
			Board.PageInfo(page,15, entries.size, ceil(entries.size / 15f).roundToInt())
		))
	}

	override suspend fun getNotice(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return Resource.Error(NotImplementedError())
	}

	override suspend fun getLectureMaterials(
		semester: String,
		subjectId: String,
		page: Int,
		criteria: BoardSearchCriteria,
		keyword: String?
	): Resource<Board> {
		delay(NETWORK_DELAY)

		return Resource.Success(
			Board(arrayOf(), Board.PageInfo(1, 0, 0, 1))
		)
	}

	override suspend fun getLectureMaterial(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return Resource.Error(NotImplementedError())
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		delay(NETWORK_DELAY)

		return Resource.Success(Board(arrayOf(), Board.PageInfo(1, 0, 0, 1)))
	}

	override suspend fun getQna(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return Resource.Error(NotImplementedError())
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Resource<List<Attachment>> {
		delay(NETWORK_DELAY)

		return Resource.Success(listOf())
	}

	override suspend fun getSyllabusList(
		year: Int,
		term: Int,
		keyword: String,
		professor: String
	): Resource<List<SyllabusSummary>> {
		delay(NETWORK_DELAY)

		val syllabuses = SYLLABUS.values.filter { it.subjectName.contains(keyword) }
		return Resource.Success(
			syllabuses.map {
				SyllabusSummary(it.division, it.course, it.subjectName, term, year, it.tutor.name, it.targetGrade,
				it.openGwamokNo, it.departmentCode, it.credits, it.lessonHours, it.summary, it.tutor.telephoneContact, it.introductionVideoUrl)
			}
		)
	}

	override suspend fun getSyllabus(subjectId: String): Resource<Syllabus> {
		delay(NETWORK_DELAY)

		val syllabus = SYLLABUS[subjectId]
		return if(syllabus == null) Resource.Error(KlasNoDataError())
			else Resource.Success(syllabus)
	}

	override suspend fun getTeachingAssistants(subjectId: String): Resource<List<TeachingAssistant>> {
		delay(NETWORK_DELAY)

		return Resource.Success(
			TEACHING_ASSISTANTS[subjectId] ?: emptyList()
		)
	}

	override suspend fun getLectureSchedules(subjectId: String): Resource<List<LectureSchedule>> {
		delay(NETWORK_DELAY)

		return Resource.Success(
			LECTURE_SCHEDULES[subjectId] ?: emptyList()
		)
	}

	override suspend fun getLectureStudentsNumber(subjectId: String): Resource<Int> {
		delay(NETWORK_DELAY)

		return Resource.Success(5)
	}

	override suspend fun getAssignments(
		semester: String,
		subjectId: String
	): Resource<List<AssignmentEntry>> {
		delay(NETWORK_DELAY)

		return Resource.Success(listOf())
	}

	override suspend fun getAssignment(
		semester: String,
		subjectId: String,
		order: Int
	): Resource<Assignment> {
		delay(NETWORK_DELAY)

		return Resource.Error(NotImplementedError())
	}

	override suspend fun getOnlineContentList(
		semester: String,
		subjectId: String
	): Resource<List<OnlineContentEntry>> {
		delay(NETWORK_DELAY)

		return Resource.Success(ONLINE_CONTENTS[subjectId] ?: emptyList())
	}

	override suspend fun getGrades(): Resource<List<SemesterGrade>> {
		return Resource.Success(listOf())
	}

	override suspend fun getCreditStatus(): Resource<CreditStatus> {
		return Resource.Error(NotImplementedError())
	}

	override suspend fun getSchoolRegister(): Resource<SchoolRegister> {
		return Resource.Error(NotImplementedError())
	}

	private companion object {
		const val NETWORK_DELAY = 1000L

		const val CLASSROOM_UNDEFINED = "미지정"

		const val USER_ID = "2019203999ZZ"

		val now: ZonedDateTime = ZonedDateTime.now()

		val SUBJECTS = arrayOf(
			Subject("U2021100000000015", "01", "0000-5-0000-01", "아인슈타인", "일반상대성이론실험",
				2021, 1, "2021,1", "학부", false),
			Subject("U202110000W000015", "01", "W000-5-0000-01", "히키가야 하치만", "5개기본호흡",
				2021, 1, "2021,1", "학부", false),
			Subject("U202110000Z000016", "01", "Z000-6-0000-01", "사이가 조지", "심리학과프로파일링",
				2021, 1, "2021,1", "학부", false),
			Subject("U202110000Y000015", "01", "Y000-5-0000-01", "로이드 아스프룬드", "로봇공학1",
				2021, 1, "2021,1", "학부", false),
			Subject("U202110000H000015", "01", "H000-5-0000-01", "카야바 아키히코", "가상현실",
				2021, 1, "2021,1", "학부", false)
		)

		val SEMESTERS = listOf(
			Semester("2021,1", "2021년도 1학기", SUBJECTS.map {
				BriefSubject(it.id, "${it.name} - ${it.professor}", it.name)
			})
		)

		val CURRENT_SEMESTER = SEMESTERS[0]

		val NOTICES = mapOf(
			"U2021100000000015" to listOf(
				BriefNotice("U2021100000000015", "01", "-", 2021, 1,
					ZonedDateTime.ofInstant(Instant.ofEpochMilli(1598918400L), ZoneId.of("Asia/Seoul")),
					"일반상대성이론실험", "사건의 지평선 실험1 유의사항",
					61, "강의 공지사항", "2021,1"
				)
			)
		)

		val TEACHING_ASSISTANTS = mapOf(
			"U202110000W000015" to listOf(
				TeachingAssistant("유키노시타 유키노", "ooook@shipduck.net", "U202110000W000015")
			)
		)

		val ONLINE_CONTENTS = mapOf(
			"U2021100000000015" to listOf(
				OnlineContentEntry.Homework(
					"proj", null, ZonedDateTime.now(), now + Duration.ofHours(2),
					"사건의 지평선 실험1", 40, 0
				),
				OnlineContentEntry.Homework(
					"proj", null, ZonedDateTime.now(), now + Duration.ofMinutes(30),
					"시공간 왜곡 관찰 보고서", 40, 0
				),
				OnlineContentEntry.Video(
					"lesson", "01", "사건의 지평선 1", now, now, now + Duration.ofMinutes(30),
					0, 40, 0, "https://example.com/video1.mp4"
				)
			)
		)

		const val DAY_MONDAY = 1
		const val DAY_TUESDAY = 2
		const val DAY_WEDNESDAY = 3
		const val DAY_THURSDAY = 4
		const val DAY_FRIDAY = 5

		val LECTURE_SCHEDULES = mapOf(
			"U2021100000000015" to listOf(
				LectureSchedule(DAY_FRIDAY, "금", "M87", intArrayOf(1, 2))
			),
			"U202110000W000015" to listOf(
				LectureSchedule(DAY_TUESDAY, "화", null, intArrayOf(1, 2, 3, 4)),
				LectureSchedule(DAY_THURSDAY, "목", null, intArrayOf(1, 2, 3, 4))
			),
			"U202110000Z000016" to listOf(
				LectureSchedule(DAY_MONDAY, "월", null, intArrayOf(4)),
				LectureSchedule(DAY_WEDNESDAY, "수", null, intArrayOf(3))
			),
			"U202110000Y000015" to listOf(
				LectureSchedule(DAY_MONDAY, "월", null, intArrayOf(2)),
				LectureSchedule(DAY_WEDNESDAY, "수", null, intArrayOf(1))
			),
			"U202110000H000015" to listOf(
				LectureSchedule(DAY_TUESDAY, "화", null, intArrayOf(5)),
				LectureSchedule(DAY_THURSDAY, "목", null, intArrayOf(6))
			)
		)

		val SYLLABUS = mapOf(
			"U202110000W000015" to Syllabus(
				"5개기본호흡", "5 Fundamental Breathing", "W000", 5,
				"0000", "01", "전선", null, 3, 4,
				Tutor("히키가야 하치만", "박사", "", "", "whatsthat@shipduck.net"),
				false, 0, false, 0,
				"호흡법 중 기본 계파인 물, 번개, 바람, 화염, 바위의 호흡에 대해서 학습한다. 또한 전집중 호흡의 경지에 올라섬으로써 오니의 급습에 항시 대비할 수 있도록 한다.",
				"전집중 호흡법을 체득하여 기본적인 체력을 증진시킴과 동시에 오니의 공격에 불시에 대항할 수 있도록 하는 것이 본 강의의 목표이다.",
				listOf(Expectation("전집중 호흡", "전집중 호흡을 습득한다."), Expectation("기본 호흡", "5가지의 기본 호흡에 대한 개요를 학습한다.")),
				Credits(1, 2, 0), null, "엑스칼리버",
				LectureType.TEAM_TEACHING or LectureType.APPRENTICESHIP or LectureType.SMALL,
				LectureMethod.FACE_TO_FACE_100, VL(30, 0, 0, 0, 30, 30, 10),
				ScoreWeights(10, 30, 30, 20, 10, 0, 0),
				listOf(Book("자민당, 폭발해라", "히키가야 하치만", "쇼가쿠칸", 2016), Book("역시 내 나라인 일본의 정치는 잘못되어 있다.", "히키가야 하치만", "쇼가쿠칸", 2016)),
				"교재를 구매하지 않으면 강의를 진행할 수 없음.",
				listOf(
					Week(1, "과목 소개 (OT)", "", null),
					Week(2, "물의 호흡 1", "", null),
					Week(3, "물의 호흡 2", "", null),
					Week(4, "화염의 호흡 1", "", null),
					Week(5, "화염의 호흡 2", "", null),
					Week(6, "바람의 호흡 1", "", null),
					Week(7, "중간고사", "", null),
					Week(8, "바람의 호흡 2", "", null)
				)
			),
			"U2021100000000015" to Syllabus(
				"일반상대성이론실험", "Theory of General Relativity Lab", "0000", 5,
				"0000", "01", "전선", null, 3, 4,
				Tutor("아인슈타인", "교수", null, null, ""),
				false, 0, false, 0,
				"일반상대성이론 실험", "",
				listOf(Expectation("이론", "일반상대성이론의 이론적 배경에 대한 이해")),
				Credits(1, 2, 0), null, null,
				LectureType.EXPERIMENT, LectureMethod.FACE_TO_FACE_100, VL(40, 40, 10, 0, 0, 10, 0),
				ScoreWeights(10, 20, 20, 40, 5, 5, 0),
				listOf(Book("프린트 제공", null, null, null)), null,
				listOf(
					Week(1, "과목 소개 (OT)", "", null)
				)
			)
		)

		fun findSubject(subjectId: String): Subject? {
			return SUBJECTS.find { it.id == subjectId }
		}
	}
}
