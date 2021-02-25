package org.openklas.klas

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

import android.util.Base64
import com.google.gson.Gson
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.klas.model.Attachment
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
import org.openklas.utils.Result
import org.openklas.utils.validateSession
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject
import kotlin.random.Random

class DefaultKlasClient @Inject constructor(
	private val service: KlasService,
	private val gson: Gson
): KlasClient {
	override suspend fun login(username: String, password: String): Result<String> {
		val security = service.loginSecurity()

		val keyFactory = KeyFactory.getInstance("RSA")
		val keySpec = X509EncodedKeySpec(Base64.decode(security.publicKey, Base64.DEFAULT))
		val publicKey = keyFactory.generatePublic(keySpec)

		val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
		cipher.init(Cipher.ENCRYPT_MODE, publicKey)

		val cipherText = cipher.doFinal(gson.toJson(mapOf(
			"loginId" to username,
			"loginPwd" to password,
			"storeIdYn" to "N"
		)).toByteArray())

		return when(val result = service.loginConfirm(mapOf(
			"loginToken" to Base64.encodeToString(cipherText, Base64.NO_WRAP),
			"redirectUrl" to "",
			"redirectTabUrl" to ""
		)).validateSession()) {
			is Result.Success -> {
				val value = result.value

				if(value.errorCount > 0)
					Result.Error(KlasSigninFailError("failed login attempt"))
				else
					Result.Success(value.response.userId)
			}
			is Result.Error -> Result.Error(result.error)
		}
	}

	override suspend fun getHome(semester: String): Result<Home> {
		return service.home(RequestHome(semester)).validateSession()
	}

	override suspend fun getSemesters(): Result<Array<Semester>> {
		return service.semesters().validateSession()
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return service.notices(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getNotice(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return service.notice(RequestPostContent(boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return service.materials(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getLectureMaterial(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return service.material(RequestPostContent(boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return service.qnas(RequestPostList(
			page = page, subject = subjectId, semester = semester, searchCriteria = criteria, keyword = keyword
		)).validateSession()
	}

	override suspend fun getQna(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return service.qna(RequestPostContent(boardNo = boardNo, masterNo = masterNo)).validateSession()
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Result<Array<Attachment>> {
		return service.attachments(RequestAttachments(storageId = storageId, attachmentId = attachmentId))
			.validateSession()
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Result<Array<SyllabusSummary>> {
		return service.syllabusList(RequestSyllabusSummary(
			year = year, term = term, keyword = keyword, professor = professor)).validateSession()
	}

	override suspend fun getSyllabus(subjectId: String): Result<Syllabus> {
		return service.syllabus(RequestSyllabus(
			subjectId = subjectId
		)).validateSession()
	}

	override suspend fun getTeachingAssistants(subjectId: String): Result<Array<TeachingAssistant>> {
		return service.teachingAssistants(RequestTeachingAssistant(subjectId = subjectId)).validateSession()
	}

	override suspend fun getLectureSchedules(subjectId: String): Result<Array<LectureSchedule>> {
		return service.lectureSchedules(RequestLectureSchedules(subjectId = subjectId)).validateSession()
	}

	override suspend fun getLectureStudentsNumber(subjectId: String): Result<Int> {
		val number = Random.nextInt(1000, 10000)

		return when(val result = service.lectureStudentsNumber(RequestLectureStudents(
			subjectId = subjectId, randomNum = number, numText = number
		)).validateSession()) {
			is Result.Success -> Result.Success(result.value.students)
			is Result.Error -> Result.Error(result.error)
		}
	}

	override suspend fun getOnlineContentList(semester: String, subjectId: String): Result<Array<OnlineContentEntry>> {
		return service.onlineContentList(RequestOnlineContents(
			semester = semester, subjectId = subjectId
		)).validateSession()
	}
}
