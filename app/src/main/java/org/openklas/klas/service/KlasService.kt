package org.openklas.klas.service

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
import org.openklas.klas.KlasUri
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
import org.openklas.klas.response.ResponseLectureStudents
import org.openklas.klas.response.ResponseLoginConfirm
import org.openklas.klas.response.ResponseLoginSecurity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KlasService {
	@POST(KlasUri.LOGIN_SECURITY)
	fun loginSecurity(): Single<ResponseLoginSecurity>

	@POST(KlasUri.LOGIN_CONFIRM)
	fun loginConfirm(@Body payload: Map<String, String>): Single<Response<ResponseLoginConfirm>>

	@POST(KlasUri.STD_HOME)
	suspend fun home(@Body payload: RequestHome): Response<Home>

	@POST(KlasUri.STD_SEMESTERS)
	fun semesters(@Body payload: Any = mapOf<Nothing, Nothing>()): Single<Response<Array<Semester>>>

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
	suspend fun attachments(@Body payload: RequestAttachments): Response<Array<Attachment>>

	@POST(KlasUri.STD_SYLLABUS_LIST)
	suspend fun syllabusList(@Body payload: RequestSyllabusSummary): Response<Array<SyllabusSummary>>

	@POST(KlasUri.STD_SYLLABUS)
	fun syllabus(@Body payload: RequestSyllabus): Single<Response<Syllabus>>

	@POST(KlasUri.STD_TEACHING_ASSISTANT)
	fun teachingAssistants(@Body payload: RequestTeachingAssistant): Single<Response<Array<TeachingAssistant>>>

	@POST(KlasUri.STD_LECTURE_SCHEDULE)
	fun lectureSchedules(@Body payload: RequestLectureSchedules): Single<Response<Array<LectureSchedule>>>

	@POST(KlasUri.STD_LECTURE_STUDENTS)
	fun lectureStudentsNumber(@Body payload: RequestLectureStudents): Single<Response<ResponseLectureStudents>>

	@POST(KlasUri.STD_ONLINE_CONTENT_LIST)
	suspend fun onlineContentList(@Body payload: RequestOnlineContents): Response<Array<OnlineContentEntry>>
}
