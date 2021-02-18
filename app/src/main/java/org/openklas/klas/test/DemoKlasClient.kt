package org.openklas.klas.test

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

import io.reactivex.Single
import org.openklas.klas.KlasClient
import org.openklas.klas.model.Board
import org.openklas.klas.model.Book
import org.openklas.klas.model.BriefNotice
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Credits
import org.openklas.klas.model.Expectation
import org.openklas.klas.model.Home
import org.openklas.klas.model.LectureMethod
import org.openklas.klas.model.LectureSchedule
import org.openklas.klas.model.LectureType
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Professor
import org.openklas.klas.model.ScoreWeights
import org.openklas.klas.model.Semester
import org.openklas.klas.model.Subject
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.model.TeachingAssistant
import org.openklas.klas.model.Timetable
import org.openklas.klas.model.Tutor
import org.openklas.klas.model.VL
import org.openklas.klas.model.Week
import org.openklas.klas.request.BoardSearchCriteria
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DemoKlasClient @Inject constructor(): KlasClient {
	override fun login(username: String, password: String): Single<String> {
		return Single.timer(0, TimeUnit.MILLISECONDS).map {
			"2019203999ZZ"
		}
	}

	override fun getHome(semester: String): Single<Home> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			Home(
				subjects = arrayOf(
					Subject("U202012345678", "01", "0000-1-4567-01",
						"아인슈타인", "일반상대성이론실험", 2020, 2, "2020,2", "학부", false)
				),
				professor = Professor("010-1234-5678", "", "", "", null),
				notices = arrayOf(
					BriefNotice("U202012345678", "01", "-", 2020, 2,
						Date(1598918400L), "일반상대성이론실험", "사건의 지평선 실험1 유의사항",
						61, "강의 공지사항", "2020,2"
					)
				),
				semesterLabel = "2020년도 2학기",
				timetable = Timetable(arrayOf(
					Timetable.Entry(5 ,4, "M87", "아인슈타인", 3,
						"일반상대성이론실험", "U202012345678", "2020,2", 1)
				))
			)
		}
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			arrayOf(
				Semester("2020,2", "2020년도 2학기", arrayOf(
					BriefSubject("U202012345678", "일반상대성이론실험 - 아인슈타인", "일반상대성이론실험")
				))
			)
		}
	}

	override fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			Board(Array(15) {
				Board.Entry(
					null, -1, "01", 0, 0, "01", 1, 5,
					isMine = false, isPublic = true, 10, 0, Date(), "-", 0, "U202012345678",
					"사건의 지평선 접근시 주의사항 ${15 * page + it}", isPinned = false, "아인슈타인", 2020
				)
			}, Board.PageInfo(page, 15, 75, 5))
		}
	}

	override fun getLectureMaterials(
		semester: String,
		subjectId: String,
		page: Int,
		criteria: BoardSearchCriteria,
		keyword: String?
	): Single<Board> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			Board(arrayOf(), Board.PageInfo(1, 0, 0, 1))
		}
	}

	override fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			Thread.sleep(NETWORK_DELAY)

			Board(arrayOf(), Board.PageInfo(1, 0, 0, 1))
		}
	}

	override fun getSyllabusList(
		year: Int,
		term: Int,
		keyword: String,
		professor: String
	): Single<Array<SyllabusSummary>> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			arrayOf(
				SyllabusSummary(
					"01", "전선", "5개기본호흡", 1, 2021, "히키가야 하치만",
					5, "1234", "W000", 3, 4,
					"호흡법의 기본 계파 화염, 물, 번개, 바위, 바람의 형에 대해 학습한다",
					"+818000000000", null
				),
				SyllabusSummary(
					"01", "전필", "일반상대성이론실험", 1, 2021, "아인슈타인",
					2, "1234", "0000", 3, 4,
					"일반상대성이론과 관련된 실험을 수행한다",
					null, null
				)
			)
		}
	}

	override fun getSyllabus(subjectId: String): Single<Syllabus> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			Syllabus(
				"5개기본호흡", "5 Fundamental Breathing", "W000", 5,
				"1234", "01", "전선", null, 3, 4,
				Tutor("히키가야 하치만", "박사", "", "", "whatsthat@shipduck.net"),
				false, 0, false, 0,
				"호흡법 중 기본 계파인 물, 번개, 바람, 화염, 바위의 호흡에 대해서 학습한다. 또한 전집중 호흡의 경지에 올라섬으로써 오니의 급습에 항시 대비할 수 있도록 한다.",
				"전집중 호흡법을 체득하여 기본적인 체력을 증진시킴과 동시에 오니의 공격에 불시에 대항할 수 있도록 하는 것이 본 강의의 목표이다.",
				arrayOf(Expectation("전집중 호흡", "전집중 호흡을 습득한다."), Expectation("기본 호흡", "5가지의 기본 호흡에 대한 개요를 학습한다.")),
				Credits(1, 2, 0), null, "엑스칼리버",
				LectureType.TEAM_TEACHING or LectureType.APPRENTICESHIP or LectureType.SMALL,
				LectureMethod.FACE_TO_FACE_100, VL(30, 0, 0, 0, 30, 30, 10),
				ScoreWeights(10, 30, 30, 20, 10, 0, 0),
				arrayOf(Book("자민당, 폭발해라", "히키가야 하치만", "쇼가쿠칸", 2016), Book("역시 내 나라인 일본의 정치는 잘못되어 있다.", "히키가야 하치만", "쇼가쿠칸", 2016)),
				"교재를 구매하지 않으면 강의를 진행할 수 없음.",
				arrayOf(
					Week(1, "과목 소개 (OT)", "", null),
					Week(2, "물의 호흡 1", "", null),
					Week(3, "물의 호흡 2", "", null),
					Week(4, "화염의 호흡 1", "", null),
					Week(5, "화염의 호흡 2", "", null),
					Week(6, "바람의 호흡 1", "", null),
					Week(7, "중간고사", "", null),
					Week(8, "바람의 호흡 2", "", null)
				)
			)
		}
	}

	override fun getTeachingAssistants(subjectId: String): Single<Array<TeachingAssistant>> {
		val assistants = arrayOf(
			TeachingAssistant("유키노시타 유키노", "ooook@shipduck.net", "U202111234W000015")
		)

		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			assistants.filter { it.subject == subjectId }.toTypedArray()
		}
	}

	override fun getLectureSchedules(subjectId: String): Single<Array<LectureSchedule>> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			arrayOf(
				LectureSchedule(1, "월", null, intArrayOf(2, 3, 4, 5))
			)
		}
	}

	override fun getLectureStudentsNumber(subjectId: String): Single<Int> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			5
		}
	}

	override fun getOnlineContentList(
		semester: String,
		subjectId: String
	): Single<Array<OnlineContentEntry>> {
		return Single.timer(NETWORK_DELAY, TimeUnit.MILLISECONDS).map {
			val now = Date()

			arrayOf(
				OnlineContentEntry.Homework(
					"proj", null, now, Date(now.time + TimeUnit.HOURS.toMillis(2)),
					"사건의 지평선 실험1", 40, 0
				),
				OnlineContentEntry.Homework(
					"proj", null, now, Date(now.time + TimeUnit.MINUTES.toMillis(30)),
					"시공간 왜곡 관찰 보고서", 40, 0
				),
				OnlineContentEntry.Video(
					"lesson", "01", "사건의 지평선 1", now, now, Date(now.time + TimeUnit.MINUTES.toMillis(30)),
					0, 40, 0, "https://example.com/video1.mp4"
				)
			)
		}
	}

	companion object {
		const val NETWORK_DELAY = 1000L
	}
}
