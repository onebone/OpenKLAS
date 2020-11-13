package org.openklas.klas.service

import io.reactivex.Single
import org.openklas.klas.KlasUri
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import org.openklas.klas.request.RequestHome
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
}
