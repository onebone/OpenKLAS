package org.openklas.klas

import android.util.Base64
import com.google.gson.Gson
import io.reactivex.Single
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import org.openklas.klas.request.RequestHome
import org.openklas.klas.service.KlasService
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class KlasClient @Inject constructor(
	private val service: KlasService,
	private val gson: Gson
) {
	private var semesters: Array<Semester>? = null

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

			confirmResponse.flatMap<String> { confirm ->
				if(confirm.errorCount > 0) {
					Single.error(KlasSigninFailError("failed login attempt"))
				}else{
					Single.just(confirm.response.userId)
				}
			}
		}
	}

	fun getHome(semester: String): Single<Home> {
		return service.home(RequestHome(semester))
	}

	fun getSemesters(): Single<Array<Semester>> {
		synchronized(this) {
			if (semesters != null) return Single.just(semesters)
		}

		return service.semesters().doOnSuccess {
			synchronized(this) {
				semesters = it
			}
		}
	}
}
