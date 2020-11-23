package org.openklas.data

import io.reactivex.Single
import org.openklas.klas.KlasClient
import javax.inject.Inject

class RemoteSessionDataSource @Inject constructor(
	private val klas: KlasClient
): SessionDataSource {
	override fun tryLogin(username: String, password: String): Single<Boolean> {
		@Suppress("RedundantLambdaArrow", "RemoveExplicitTypeArguments")
		return klas.login(username, password)
			.flatMap {
				Single.just<String?>(it)
			}
			.onErrorResumeNext { Single.just<String?>(null) }
			.map { it: String? ->
				it != null
			}
	}
}
