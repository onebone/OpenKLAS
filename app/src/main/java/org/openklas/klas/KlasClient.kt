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

	fun getHome(semester: String): Single<Home>

	fun getSemesters(): Single<Array<Semester>>

	fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>

	fun getNotice(boardNo: Int, masterNo: Int): Single<PostComposite>

	fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>

	fun getLectureMaterial(boardNo: Int, masterNo: Int): Single<PostComposite>

	fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Single<Board>

	fun getQna(boardNo: Int, masterNo: Int): Single<PostComposite>

	suspend fun getAttachments(storageId: String, attachmentId: String): Result<Array<Attachment>>

	fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Single<Array<SyllabusSummary>>

	fun getSyllabus(subjectId: String): Single<Syllabus>

	fun getTeachingAssistants(subjectId: String): Single<Array<TeachingAssistant>>

	fun getLectureSchedules(subjectId: String): Single<Array<LectureSchedule>>

	fun getLectureStudentsNumber(subjectId: String): Single<Int>

	fun getOnlineContentList(semester: String, subjectId: String): Single<Array<OnlineContentEntry>>
}
