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
import org.openklas.klas.KlasClient
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
import javax.inject.Inject

class RemoteKlasDataSource @Inject constructor(
	private val klas: KlasClient
): KlasDataSource {
	override fun performLogin(username: String, password: String): Single<String> {
		return klas.login(username, password)
	}

	override suspend fun getHome(semester: String): Result<Home> {
		return klas.getHome(semester)
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return klas.getSemesters()
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klas.getNotices(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getNotice(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klas.getNotice(boardNo, masterNo)
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klas.getQnas(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getQna(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klas.getQna(boardNo, masterNo)
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klas.getLectureMaterials(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getLectureMaterial(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klas.getLectureMaterial(boardNo, masterNo)
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Result<Array<Attachment>> {
		return klas.getAttachments(storageId, attachmentId)
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Result<Array<SyllabusSummary>> {
		return klas.getSyllabusList(year, term, keyword, professor)
	}

	override fun getSyllabus(subjectId: String): Single<Syllabus> {
		return klas.getSyllabus(subjectId)
	}

	override fun getTeachingAssistants(subjectId: String): Single<Array<TeachingAssistant>> {
		return klas.getTeachingAssistants(subjectId)
	}

	override fun getLectureSchedules(subjectId: String): Single<Array<LectureSchedule>> {
		return klas.getLectureSchedules(subjectId)
	}

	override fun getLectureStudentsNumber(subjectId: String): Single<Int> {
		return klas.getLectureStudentsNumber(subjectId)
	}

	override suspend fun getOnlineContentList(semester: String, subjectId: String): Result<Array<OnlineContentEntry>> {
		return klas.getOnlineContentList(semester, subjectId)
	}
}
