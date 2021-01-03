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
import org.openklas.klas.model.Board
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Semester
import org.openklas.klas.model.SyllabusSummary
import javax.inject.Inject

class RemoteKlasDataSource @Inject constructor(
	private val klas: KlasClient
): KlasDataSource {
	override fun performLogin(username: String, password: String): Single<String> {
		return klas.login(username, password)
	}

	override fun getHome(semester: String): Single<Home> {
		return klas.getHome(semester)
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return klas.getSemesters()
	}

	override fun getNotices(semester: String, subjectId: String, page: Int): Single<Board> {
		return klas.getNotices(semester, subjectId, page)
	}

	override fun getQnas(semester: String, subjectId: String, page: Int): Single<Board> {
		return klas.getQnas(semester, subjectId, page)
	}

	override fun getLectureMaterials(semester: String, subjectId: String, page: Int): Single<Board> {
		return klas.getLectureMaterials(semester, subjectId, page)
	}

	override fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String)
		: Single<Array<SyllabusSummary>> {
		return klas.getSyllabusList(year, term, keyword, professor)
	}

	override fun getOnlineContentList(semester: String, subjectId: String)
		: Single<Array<OnlineContentEntry>> {
		return klas.getOnlineContentList(semester, subjectId)
	}
}
