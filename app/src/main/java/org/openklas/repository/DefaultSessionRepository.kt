package org.openklas.repository

import io.reactivex.Single
import org.openklas.data.AccountDataSource
import org.openklas.data.SessionDataSource
import javax.inject.Inject

class DefaultSessionRepository @Inject constructor(
	private val sessionDataSource: SessionDataSource,
	private val accountDataSource: AccountDataSource
): SessionRepository {
	override fun tryLogin(): Single<Boolean> {
		val account = accountDataSource.getAccount() ?: return Single.just(false)

		return sessionDataSource.tryLogin(account.username, account.password)
	}
}
