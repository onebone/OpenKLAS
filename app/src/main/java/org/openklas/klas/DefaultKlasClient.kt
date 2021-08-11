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

package org.openklas.klas

import android.util.Base64
import org.openklas.klas.error.KlasSigninFailError
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
import org.openklas.klas.request.RequestAssignment
import org.openklas.klas.request.RequestAssignments
import org.openklas.klas.request.RequestAttachments
import org.openklas.klas.request.RequestHome
import org.openklas.klas.request.RequestLectureSchedules
import org.openklas.klas.request.RequestLectureStudents
import org.openklas.klas.request.RequestOnlineContents
import org.openklas.klas.request.RequestPostContent
import org.openklas.klas.request.RequestPostList
import org.openklas.klas.request.RequestSyllabus
import org.openklas.klas.request.RequestSyllabusSummary
import org.openklas.klas.request.RequestTeachingAssistant
import org.openklas.klas.service.KlasService
import org.openklas.utils.Resource
import org.openklas.utils.validateSession
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DefaultKlasClient @Inject constructor(
	private val service: KlasService,
	private val json: Json
): KlasClient {
	override suspend fun testSession(): Boolean {
		val result = service.testSession()
		return when(result.validateSession()) {
			is Resource.Success -> true
			is Resource.Error -> false
		}
	}

	override suspend fun login(username: String, password: String): Resource<String> {
		val security = service.loginSecurity()

		val keyFactory = KeyFactory.getInstance("RSA")
		val keySpec = X509EncodedKeySpec(Base64.decode(security.publicKey, Base64.DEFAULT))
		val publicKey = keyFactory.generatePublic(keySpec)

		val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
		cipher.init(Cipher.ENCRYPT_MODE, publicKey)

		val cipherText = cipher.doFinal(json.encodeToString(mapOf(
			"loginId" to username,
			"loginPwd" to password,
			"storeIdYn" to "N"
		)).toByteArray())

		return when(val result = service.loginConfirm(mapOf(
			"loginToken" to Base64.encodeToString(cipherText, Base64.NO_WRAP),
			"redirectUrl" to "",
			"redirectTabUrl" to ""
		)).validateSession()) {
			is Resource.Success -> {
				val value = result.value

				if(value.errorCount > 0)
					Resource.Error(KlasSigninFailError("failed login attempt"))
				else
					Resource.Success(value.response.userId)
			}
			is Resource.Error -> Resource.Error(result.error)
		}
	}

	override suspend fun getHome(semester: String): Resource<Home> {
		return service.home(RequestHome(semester)).validateSession()
	}

	override suspend fun getSemesters(): Resource<List<Semester>> {
		return service.semesters().validateSession()
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return service.notices(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getNotice(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return service.notice(RequestPostContent(semester = semester, subject = subjectId, boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return service.materials(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getLectureMaterial(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return service.material(RequestPostContent(semester = semester, subject = subjectId, boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Resource<Board> {
		return service.qnas(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getQna(semester: String, subjectId: String, boardNo: Int, masterNo: Int): Resource<PostComposite> {
		return service.qna(RequestPostContent(semester = semester, subject = subjectId, boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Resource<List<Attachment>> {
		return service.attachments(RequestAttachments(storageId = storageId, attachmentId = attachmentId))
			.validateSession()
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Resource<List<SyllabusSummary>> {
		return service.syllabusList(RequestSyllabusSummary(
			year = year, term = term, keyword = keyword, professor = professor)).validateSession()
	}

	override suspend fun getSyllabus(subjectId: String): Resource<Syllabus> {
		return service.syllabus(RequestSyllabus(
			subjectId = subjectId
		)).validateSession()
	}

	override suspend fun getTeachingAssistants(subjectId: String): Resource<List<TeachingAssistant>> {
		return service.teachingAssistants(RequestTeachingAssistant(subjectId = subjectId)).validateSession()
	}

	override suspend fun getLectureSchedules(subjectId: String): Resource<List<LectureSchedule>> {
		return service.lectureSchedules(RequestLectureSchedules(subjectId = subjectId)).validateSession()
	}

	override suspend fun getLectureStudentsNumber(subjectId: String): Resource<Int> {
		val number = Random.nextInt(1000, 10000)

		return when(val result = service.lectureStudentsNumber(RequestLectureStudents(
			subjectId = subjectId, randomNum = number, numText = number
		)).validateSession()) {
			is Resource.Success -> Resource.Success(result.value.students)
			is Resource.Error -> Resource.Error(result.error)
		}
	}

	override suspend fun getAssignments(
		semester: String,
		subjectId: String
	): Resource<List<AssignmentEntry>> {
		return service.assignments(RequestAssignments(semester = semester, subjectId = subjectId)).validateSession()
	}

	override suspend fun getAssignment(
		semester: String,
		subjectId: String,
		order: Int
	): Resource<Assignment> {
		return service.assignment(RequestAssignment(semester = semester, subjectId = subjectId, order = order)).validateSession()
	}

	override suspend fun getOnlineContentList(semester: String, subjectId: String): Resource<List<OnlineContentEntry>> {
		return service.onlineContentList(RequestOnlineContents(
			semester = semester, subjectId = subjectId
		)).validateSession()
	}

	override suspend fun getGrades(): Resource<List<SemesterGrade>> {
		return service.grades().validateSession()
	}

	override suspend fun getCreditStatus(): Resource<CreditStatus> {
		return service.creditStatus().validateSession()
	}

	override suspend fun getSchoolRegister(): Resource<SchoolRegister> {
		return service.schoolRegister().validateSession()
	}
}
