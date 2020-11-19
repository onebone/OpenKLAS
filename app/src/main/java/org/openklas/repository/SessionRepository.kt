package org.openklas.repository

import io.reactivex.Single

interface SessionRepository {
	fun tryLogin(): Single<Boolean>
}
