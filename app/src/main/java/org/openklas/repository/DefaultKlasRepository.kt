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

package org.openklas.repository

import org.openklas.data.KlasDataSource
import org.openklas.data.CredentialDataSource
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

class DefaultKlasRepository @Inject constructor(
	private val klasDataSource: KlasDataSource,
	private val credentialDataSource: CredentialDataSource
): KlasRepository {
	override suspend fun performLogin(username: String, password: String, rememberMe: Boolean): Resource<String> {
		val result = klasDataSource.performLogin(username, password)

		if(rememberMe) {
			if(result is Resource.Success) {
				credentialDataSource.userID = username
				credentialDataSource.password = password
			}
		}

		return result
	}

	override suspend fun getHome(semester: String): Resource<Home> {
		return klasDataSource.getHome(semester)
	}

	override suspend fun getSemesters(): Resource<List<Semester>> {
		return klasDataSource.getSemesters()
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klasDataSource.getNotices(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getNotice(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klasDataSource.getNotice(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klasDataSource.getQnas(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getQna(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klasDataSource.getQna(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return klasDataSource.getLectureMaterials(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getLectureMaterial(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return klasDataSource.getLectureMaterial(semester, subjectId, boardNo, masterNo)
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Resource<List<Attachment>> {
		return klasDataSource.getAttachments(storageId, attachmentId)
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Resource<List<SyllabusSummary>> {
		return klasDataSource.getSyllabusList(year, term, keyword, professor)
	}

	override suspend fun getSyllabus(subjectId: String): Resource<Syllabus> {
		return klasDataSource.getSyllabus(subjectId)
	}

	override suspend fun getTeachingAssistants(subjectId: String): Resource<List<TeachingAssistant>> {
		return klasDataSource.getTeachingAssistants(subjectId)
	}

	override suspend fun getLectureSchedules(subjectId: String): Resource<List<LectureSchedule>> {
		return klasDataSource.getLectureSchedules(subjectId)
	}

	override suspend fun getLectureStudentsNumber(subjectId: String): Resource<Int> {
		return klasDataSource.getLectureStudentsNumber(subjectId)
	}

	override suspend fun getAssignments(
		semester: String,
		subjectId: String
	): Resource<List<AssignmentEntry>> {
		return klasDataSource.getAssignments(semester, subjectId)
	}

	override suspend fun getAssignment(
		semester: String,
		subjectId: String,
		order: Int
	): Resource<Assignment> {
		return klasDataSource.getAssignment(semester, subjectId, order)
	}

	override suspend fun getOnlineContentList(semester: String, subjectId: String): Resource<List<OnlineContentEntry>> {
		return klasDataSource.getOnlineContentList(semester, subjectId)
	}

	override suspend fun getGrades(): Resource<List<SemesterGrade>> {
		return klasDataSource.getGrades()
	}

	override suspend fun getCreditStatus(): Resource<CreditStatus> {
		return klasDataSource.getCreditStatus()
	}

	override suspend fun getSchoolRegister(): Resource<SchoolRegister> {
		return klasDataSource.getSchoolRegister()
	}
}
