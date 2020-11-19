package org.openklas.data

import io.reactivex.Single

interface SessionDataSource {
	fun tryLogin(username: String, password: String): Single<Boolean>
}
