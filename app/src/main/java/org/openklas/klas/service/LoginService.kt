package org.openklas.klas.service

import io.reactivex.Single
import org.openklas.klas.KlasUri
import org.openklas.klas.response.ResponseLoginConfirm
import org.openklas.klas.response.ResponseLoginSecurity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
	@POST(KlasUri.LOGIN_SECURITY)
	fun loginSecurity(): Single<ResponseLoginSecurity>

	@POST(KlasUri.LOGIN_CONFIRM)
	fun loginConfirm(@Body payload: Map<String, String>): Single<ResponseLoginConfirm>
}
