package org.openklas.klas

import android.util.Base64
import com.google.gson.Gson
import io.reactivex.Observable
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.klas.service.LoginService
import retrofit2.Retrofit
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class KlasClient @Inject constructor(
	private val retrofit: Retrofit,
	private val gson: Gson
) {
	fun login(username: String, password: String): Observable<String> {
		val service = retrofit.create(LoginService::class.java)

		val securityResponse = service.loginSecurity()
		return securityResponse.toObservable()
			.flatMap { security ->
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

				confirmResponse.toObservable()
					.flatMap<String> innerFlatMap@{ confirm ->
						if(confirm.errorCount > 0) {
							return@innerFlatMap Observable.error(KlasSigninFailError("failed login attempt"))
						}

						Observable.just(confirm.response.userId)
					}
			}
	}
}
