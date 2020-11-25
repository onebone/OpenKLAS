package org.openklas.klas.service

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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
import org.openklas.klas.model.Board
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import org.openklas.klas.request.RequestHome
import org.openklas.klas.request.RequestPostList
import org.openklas.klas.response.ResponseLoginConfirm
import org.openklas.klas.response.ResponseLoginSecurity
import retrofit2.http.Body
import retrofit2.http.POST

interface KlasService {
	@POST(KlasUri.LOGIN_SECURITY)
	fun loginSecurity(): Single<ResponseLoginSecurity>

	@POST(KlasUri.LOGIN_CONFIRM)
	fun loginConfirm(@Body payload: Map<String, String>): Single<ResponseLoginConfirm>

	@POST(KlasUri.STD_HOME)
	fun home(@Body payload: RequestHome): Single<Home>

	@POST(KlasUri.STD_SEMESTERS)
	fun semesters(@Body payload: Any = mapOf<Nothing, Nothing>()): Single<Array<Semester>>

	@POST(KlasUri.STD_NOTICE_LIST)
	fun notices(@Body payload: RequestPostList): Single<Board>

	@POST(KlasUri.STD_MATERIAL_LIST)
	fun materials(@Body payload: RequestPostList): Single<Board>

	@POST(KlasUri.STD_QNA_LIST)
	fun qnas(@Body payload: RequestPostList): Single<Board>
}
