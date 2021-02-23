package org.openklas.repository

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
import org.openklas.data.KlasDataSource
import org.openklas.data.PreferenceDataSource
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
import org.openklas.net.transformer.AsyncTransformer
import org.openklas.utils.Result
import javax.inject.Inject

class DefaultKlasRepository @Inject constructor(
	private val klasDataSource: KlasDataSource,
	private val preferenceDataSource: PreferenceDataSource
): KlasRepository {
	override fun performLogin(username: String, password: String, rememberMe: Boolean): Single<String> {
		return klasDataSource.performLogin(username, password).run {
				if(rememberMe)
					doOnSuccess {
						preferenceDataSource.userID = username
						preferenceDataSource.password = password
					}
				else
					this
			}.compose(AsyncTransformer())
	}

	override fun getHome(semester: String): Single<Home> {
		return klasDataSource.getHome(semester).compose(AsyncTransformer())
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return klasDataSource.getSemesters().compose(AsyncTransformer())
	}

	override suspend fun getNotices(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klasDataSource.getNotices(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getNotice(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klasDataSource.getNotice(boardNo, masterNo)
	}

	override suspend fun getQnas(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klasDataSource.getQnas(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getQna(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klasDataSource.getQna(boardNo, masterNo)
	}

	override suspend fun getLectureMaterials(semester: String, subjectId: String, page: Int, criteria: BoardSearchCriteria, keyword: String?): Result<Board> {
		return klasDataSource.getLectureMaterials(semester, subjectId, page, criteria, keyword)
	}

	override suspend fun getLectureMaterial(boardNo: Int, masterNo: Int): Result<PostComposite> {
		return klasDataSource.getLectureMaterial(boardNo, masterNo)
	}

	override suspend fun getAttachments(
		storageId: String,
		attachmentId: String
	): Result<Array<Attachment>> {
		return klasDataSource.getAttachments(storageId, attachmentId)
	}

	override suspend fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Result<Array<SyllabusSummary>> {
		return klasDataSource.getSyllabusList(year, term, keyword, professor)
	}

	override fun getSyllabus(subjectId: String): Single<Syllabus> {
		return klasDataSource.getSyllabus(subjectId).compose(AsyncTransformer())
	}

	override fun getTeachingAssistants(subjectId: String): Single<Array<TeachingAssistant>> {
		return klasDataSource.getTeachingAssistants(subjectId).compose(AsyncTransformer())
	}

	override fun getLectureSchedules(subjectId: String): Single<Array<LectureSchedule>> {
		return klasDataSource.getLectureSchedules(subjectId).compose(AsyncTransformer())
	}

	override fun getLectureStudentsNumber(subjectId: String): Single<Int> {
		return klasDataSource.getLectureStudentsNumber(subjectId).compose(AsyncTransformer())
	}

	override fun getOnlineContentList(semester: String, subjectId: String)
		: Single<Array<OnlineContentEntry>> {
		return klasDataSource.getOnlineContentList(semester, subjectId).compose(AsyncTransformer())
	}
}
