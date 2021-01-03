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
import org.openklas.klas.model.BriefNotice
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Professor
import org.openklas.klas.model.Semester
import org.openklas.klas.model.Subject
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.model.Timetable
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DemoKlasClient @Inject constructor(): KlasClient {
	override fun login(username: String, password: String): Single<String> {
		return Single.just("2019203999ZZ")
	}

	override fun getHome(semester: String): Single<Home> {
		return Single.just(Home(
			subjects = arrayOf(
				Subject("U202012345678", "01", "0000-1-456-7890",
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
		))
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return Single.just(arrayOf(
			Semester("2020,2", "2020년도 2학기", arrayOf(
				BriefSubject("U202012345678", "일반상대성이론실험 - 아인슈타인", "일반상대성이론실험")
			))
		))
	}

	override fun getNotices(semester: String, subjectId: String, page: Int): Single<Board> {
		return Single.just(Board(arrayOf(), Board.PageInfo(1, 0, 0, 1)))
	}

	override fun getLectureMaterials(
		semester: String,
		subjectId: String,
		page: Int
	): Single<Board> {
		return Single.just(Board(arrayOf(), Board.PageInfo(1, 0, 0, 1)))
	}

	override fun getQnas(semester: String, subjectId: String, page: Int): Single<Board> {
		return Single.just(Board(arrayOf(), Board.PageInfo(1, 0, 0, 1)))
	}

	override fun getSyllabusList(
		year: Int,
		term: Int,
		keyword: String,
		professor: String
	): Single<Array<SyllabusSummary>> {
		return Single.just(arrayOf())
	}

	override fun getOnlineContentList(
		semester: String,
		subjectId: String
	): Single<Array<OnlineContentEntry>> {
		val now = Date()

		return Single.just(arrayOf(
			OnlineContentEntry.Homework(
				"proj", null, now, Date(now.time + TimeUnit.HOURS.toMillis(2)),
				"사건의 지평선 실험1", 40, 0
			)
		))
	}
}
