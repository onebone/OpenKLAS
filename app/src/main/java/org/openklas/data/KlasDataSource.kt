package org.openklas.data

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
import org.openklas.klas.model.Board
import org.openklas.klas.model.Home
import org.openklas.klas.model.LectureSchedule
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.PostComposite
import org.openklas.klas.model.Semester
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.model.TeachingAssistant
import org.openklas.klas.request.BoardSearchCriteria

interface KlasDataSource {
	fun performLogin(username: String, password: String): Single<String>
	fun getHome(semester: String): Single<Home>
	fun getSemesters(): Single<Array<Semester>>
	fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>
	fun getNotice(boardNo: Int, masterNo: Int): Single<PostComposite>
	fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>
	fun getQna(boardNo: Int, masterNo: Int): Single<PostComposite>
	fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>
	fun getLectureMaterial(boardNo: Int, masterNo: Int): Single<PostComposite>
	fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Single<Array<SyllabusSummary>>
	fun getSyllabus(subjectId: String): Single<Syllabus>
	fun getTeachingAssistants(subjectId: String): Single<Array<TeachingAssistant>>
	fun getLectureSchedules(subjectId: String): Single<Array<LectureSchedule>>
	fun getLectureStudentsNumber(subjectId: String): Single<Int>
	fun getOnlineContentList(semester: String, subjectId: String): Single<Array<OnlineContentEntry>>
}
