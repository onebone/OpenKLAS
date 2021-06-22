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

package org.openklas.data

import org.openklas.klas.KlasClient
import org.openklas.klas.model.Assignment
import org.openklas.klas.model.AssignmentEntry
import org.openklas.klas.model.Attachment
import org.openklas.klas.model.Board
import org.openklas.klas.model.CreditStatus
import org.openklas.klas.model.Home
import org.openklas.klas.model.LectureSchedule
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.PostComposite
import org.openklas.klas.model.SchoolRegister
import org.openklas.klas.model.Semester
import org.openklas.klas.model.SemesterGrade
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.model.TeachingAssistant
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.utils.Resource
import javax.inject.Inject

class RemoteKlasDataSource @Inject constructor(
	private val klas: KlasClient
): KlasDataSource {
	override suspend fun performLogin(username: String, password: String): Resource<String> {
		return klas.login(username, password)
	}

	override suspend fun getHome(semester: String): Resource<Home> {
		return klas.getHome(semester)
	}

	override suspend fun getSemesters(): Resource<Array<Semester>> {
		return klas.getSemesters()
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klas.getNotices(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getNotice(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klas.getNotice(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klas.getQnas(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getQna(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klas.getQna(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klas.getLectureMaterials(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getLectureMaterial(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klas.getLectureMaterial(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Resource<Array<Attachment>> {
		return klas.getAttachments(storageId, attachmentId)
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Resource<Array<SyllabusSummary>> {
		return klas.getSyllabusList(year, term, keyword, professor)
	}

	override suspend fun getSyllabus(subjectId: String): Resource<Syllabus> {
		return klas.getSyllabus(subjectId)
	}

	override suspend fun getTeachingAssistants(subjectId: String): Resource<Array<TeachingAssistant>> {
		return klas.getTeachingAssistants(subjectId)
	}

	override suspend fun getLectureSchedules(subjectId: String): Resource<Array<LectureSchedule>> {
		return klas.getLectureSchedules(subjectId)
	}

	override suspend fun getLectureStudentsNumber(subjectId: String): Resource<Int> {
		return klas.getLectureStudentsNumber(subjectId)
	}

	override suspend fun getAssignments(
		semester: String,
		subjectId: String
	): Resource<Array<AssignmentEntry>> {
		return klas.getAssignments(semester, subjectId)
	}

	override suspend fun getAssignment(
		semester: String,
		subjectId: String,
		order: Int
	): Resource<Assignment> {
		return klas.getAssignment(semester, subjectId, order)
	}

	override suspend fun getOnlineContentList(semester: String, subjectId: String): Resource<Array<OnlineContentEntry>> {
		return klas.getOnlineContentList(semester, subjectId)
	}

	override suspend fun getGrades(): Resource<List<SemesterGrade>> {
		return klas.getGrades()
	}

	override suspend fun getCreditStatus(): Resource<CreditStatus> {
		return klas.getCreditStatus()
	}

	override suspend fun getSchoolRegister(): Resource<SchoolRegister> {
		return klas.getSchoolRegister()
	}
}
