package org.openklas.klas

import io.reactivex.Single
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
import org.openklas.utils.Result

interface KlasClient {
	fun login(username: String, password: String): Single<String>

	suspend fun getHome(semester: String): Result<Home>

	fun getSemesters(): Single<Array<Semester>>

	suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board>

	suspend fun getNotice(boardNo: Int, masterNo: Int): Result<PostComposite>

	suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board>

	suspend fun getLectureMaterial(boardNo: Int, masterNo: Int): Result<PostComposite>

	suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board>

	suspend fun getQna(boardNo: Int, masterNo: Int): Result<PostComposite>

	suspend fun getAttachments(storageId: String, attachmentId: String): Result<Array<Attachment>>

	suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Result<Array<SyllabusSummary>>

	suspend fun getSyllabus(subjectId: String): Result<Syllabus>

	suspend fun getTeachingAssistants(subjectId: String): Result<Array<TeachingAssistant>>

	suspend fun getLectureSchedules(subjectId: String): Result<Array<LectureSchedule>>

	suspend fun getLectureStudentsNumber(subjectId: String): Result<Int>

	suspend fun getOnlineContentList(semester: String, subjectId: String): Result<Array<OnlineContentEntry>>
}
