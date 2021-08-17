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

package me.onebone.openklas.klas.service

import kotlinx.serialization.Serializable
import me.onebone.openklas.klas.KlasUri
import me.onebone.openklas.klas.model.Assignment
import me.onebone.openklas.klas.model.Attachment
import me.onebone.openklas.klas.model.Board
import me.onebone.openklas.klas.model.Home
import me.onebone.openklas.klas.model.AssignmentEntry
import me.onebone.openklas.klas.model.CreditStatus
import me.onebone.openklas.klas.model.LectureSchedule
import me.onebone.openklas.klas.model.OnlineContentEntry
import me.onebone.openklas.klas.model.PostComposite
import me.onebone.openklas.klas.model.SchoolRegister
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.klas.model.SemesterGrade
import me.onebone.openklas.klas.model.Syllabus
import me.onebone.openklas.klas.model.SyllabusSummary
import me.onebone.openklas.klas.model.TeachingAssistant
import me.onebone.openklas.klas.request.RequestAssignment
import me.onebone.openklas.klas.request.RequestAttachments
import me.onebone.openklas.klas.request.RequestHome
import me.onebone.openklas.klas.request.RequestAssignments
import me.onebone.openklas.klas.request.RequestLectureSchedules
import me.onebone.openklas.klas.request.RequestLectureStudents
import me.onebone.openklas.klas.request.RequestOnlineContents
import me.onebone.openklas.klas.request.RequestPostContent
import me.onebone.openklas.klas.request.RequestPostList
import me.onebone.openklas.klas.request.RequestSyllabus
import me.onebone.openklas.klas.request.RequestSyllabusSummary
import me.onebone.openklas.klas.request.RequestTeachingAssistant
import me.onebone.openklas.klas.response.ResponseLectureStudents
import me.onebone.openklas.klas.response.ResponseLoginConfirm
import me.onebone.openklas.klas.response.ResponseLoginSecurity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
class Empty

internal val EmptyObject = Empty()

interface KlasService {
	@POST(KlasUri.STD_TEST_SESSION)
	suspend fun testSession(): Response<Unit>

	@POST(KlasUri.LOGIN_SECURITY)
	suspend fun loginSecurity(): ResponseLoginSecurity

	@POST(KlasUri.LOGIN_CONFIRM)
	suspend fun loginConfirm(@Body payload: Map<String, String>): Response<ResponseLoginConfirm>

	@POST(KlasUri.STD_HOME)
	suspend fun home(@Body payload: RequestHome): Response<Home>

	@POST(KlasUri.STD_SEMESTERS)
	suspend fun semesters(@Body payload: Empty = EmptyObject): Response<List<Semester>>

	@POST(KlasUri.STD_NOTICE_LIST)
	suspend fun notices(@Body payload: RequestPostList): Response<Board>

	@POST(KlasUri.STD_NOTICE_CONTENT)
	suspend fun notice(@Body payload: RequestPostContent): Response<PostComposite>

	@POST(KlasUri.STD_MATERIAL_LIST)
	suspend fun materials(@Body payload: RequestPostList): Response<Board>

	@POST(KlasUri.STD_MATERIAL_CONTENT)
	suspend fun material(@Body payload: RequestPostContent): Response<PostComposite>

	@POST(KlasUri.STD_QNA_LIST)
	suspend fun qnas(@Body payload: RequestPostList): Response<Board>

	@POST(KlasUri.STD_QNA_CONTENT)
	suspend fun qna(@Body payload: RequestPostContent): Response<PostComposite>

	@POST(KlasUri.STD_ATTACHMENTS)
	suspend fun attachments(@Body payload: RequestAttachments): Response<List<Attachment>>

	@POST(KlasUri.STD_SYLLABUS_LIST)
	suspend fun syllabusList(@Body payload: RequestSyllabusSummary): Response<List<SyllabusSummary>>

	@POST(KlasUri.STD_SYLLABUS)
	suspend fun syllabus(@Body payload: RequestSyllabus): Response<Syllabus>

	@POST(KlasUri.STD_TEACHING_ASSISTANT)
	suspend fun teachingAssistants(@Body payload: RequestTeachingAssistant): Response<List<TeachingAssistant>>

	@POST(KlasUri.STD_LECTURE_SCHEDULE)
	suspend fun lectureSchedules(@Body payload: RequestLectureSchedules): Response<List<LectureSchedule>>

	@POST(KlasUri.STD_LECTURE_STUDENTS)
	suspend fun lectureStudentsNumber(@Body payload: RequestLectureStudents): Response<ResponseLectureStudents>

	@POST(KlasUri.STD_ASSIGNMENTS)
	suspend fun assignments(@Body payload: RequestAssignments): Response<List<AssignmentEntry>>

	@POST(KlasUri.STD_ASSIGNMENT)
	suspend fun assignment(@Body payload: RequestAssignment): Response<Assignment>

	@POST(KlasUri.STD_ONLINE_CONTENT_LIST)
	suspend fun onlineContentList(@Body payload: RequestOnlineContents): Response<List<OnlineContentEntry>>

	@POST(KlasUri.STD_GRADES)
	suspend fun grades(@Body payload: Empty = EmptyObject): Response<List<SemesterGrade>>

	@POST(KlasUri.STD_CREDIT_STATUS)
	suspend fun creditStatus(@Body payload: Empty = EmptyObject): Response<CreditStatus>

	@POST(KlasUri.STD_SCHOOL_REGISTER)
	suspend fun schoolRegister(@Body payload: Empty = EmptyObject): Response<SchoolRegister>
}
