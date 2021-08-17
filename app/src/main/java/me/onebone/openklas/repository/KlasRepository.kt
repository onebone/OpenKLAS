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

package me.onebone.openklas.repository

import me.onebone.openklas.klas.model.Assignment
import me.onebone.openklas.klas.model.AssignmentEntry
import me.onebone.openklas.klas.model.Attachment
import me.onebone.openklas.klas.model.Board
import me.onebone.openklas.klas.model.CreditStatus
import me.onebone.openklas.klas.model.Home
import me.onebone.openklas.klas.model.LectureSchedule
import me.onebone.openklas.klas.model.OnlineContentEntry
import me.onebone.openklas.klas.model.PostComposite
import me.onebone.openklas.klas.model.SchoolRegister
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.klas.model.SemesterGrade
import me.onebone.openklas.klas.model.Syllabus
import me.onebone.openklas.klas.model.SyllabusSummary
import me.onebone.openklas.klas.model.TeachingAssistant
import me.onebone.openklas.klas.request.BoardSearchCriteria
import me.onebone.openklas.utils.Resource

interface KlasRepository {
	suspend fun performLogin(username: String, password: String, rememberMe: Boolean): Resource<String>
	suspend fun getHome(semester: String): Resource<Home>
	suspend fun getSemesters(): Resource<List<Semester>>
	suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board>
	suspend fun getNotice(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite>
	suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board>
	suspend fun getQna(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite>
	suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board>
	suspend fun getLectureMaterial(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite>
	suspend fun getAttachments(storageId: String, attachmentId: String): Resource<List<Attachment>>
	suspend fun getSyllabusList(
		year: Int,
		term: Int,
		keyword: String,
		professor: String
	): Resource<List<SyllabusSummary>>

	suspend fun getSyllabus(subjectId: String): Resource<Syllabus>
	suspend fun getTeachingAssistants(subjectId: String): Resource<List<TeachingAssistant>>

	suspend fun getLectureSchedules(subjectId: String): Resource<List<LectureSchedule>>
	suspend fun getLectureStudentsNumber(subjectId: String): Resource<Int>

	suspend fun getAssignments(semester: String, subjectId: String): Resource<List<AssignmentEntry>>
	suspend fun getAssignment(semester: String, subjectId: String, order: Int): Resource<Assignment>

	suspend fun getOnlineContentList(
		semester: String,
		subjectId: String
	): Resource<List<OnlineContentEntry>>

	suspend fun getGrades(): Resource<List<SemesterGrade>>
	suspend fun getCreditStatus(): Resource<CreditStatus>
	suspend fun getSchoolRegister(): Resource<SchoolRegister>
}
