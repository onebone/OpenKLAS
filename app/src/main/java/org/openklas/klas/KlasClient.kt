package org.openklas.klas

import android.util.Base64
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import org.openklas.klas.request.RequestHome
import org.openklas.klas.service.KlasService
import org.openklas.net.compose.AsyncTransformer
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class KlasClient @Inject constructor(
	private val service: KlasService,
	private val gson: Gson
) {
	fun login(username: String, password: String): Single<String> {
		val securityResponse = service.loginSecurity()
		return securityResponse.flatMap { security ->
			val keyFactory = KeyFactory.getInstance("RSA")
			val keySpec = X509EncodedKeySpec(Base64.decode(security.publicKey, Base64.DEFAULT))
			val publicKey = keyFactory.generatePublic(keySpec)

			val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
			cipher.init(Cipher.ENCRYPT_MODE, publicKey)

			val cipherText = cipher.doFinal(gson.toJson(mapOf(
				"loginId" to username,
				"loginPwd" to password,
				"storeIdYn" to "N"
			)).toByteArray())

			val confirmResponse = service.loginConfirm(mapOf(
				"loginToken" to Base64.encodeToString(cipherText, Base64.NO_WRAP),
				"redirectUrl" to "",
				"redirectTabUrl" to ""
			))

			confirmResponse.flatMap { confirm ->
				if(confirm.errorCount > 0) {
					Single.error(KlasSigninFailError("failed login attempt"))
				}else{
					Single.just(confirm.response.userId)
				}
			}
		}
	}

	fun getHome(semester: String): Observable<Home>? {
		return service.home(RequestHome(semester)).toObservable().compose(AsyncTransformer())
	}

	fun getSemesters(): Single<Array<Semester>> {
		return service.semesters()
	}
}
