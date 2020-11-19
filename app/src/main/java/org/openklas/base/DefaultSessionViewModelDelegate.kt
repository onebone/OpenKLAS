package org.openklas.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import org.openklas.repository.SessionRepository
import javax.inject.Inject

class DefaultSessionViewModelDelegate @Inject constructor(
	sessionRepository: SessionRepository
): SessionViewModelDelegate {
	override val mustAuthenticate = MutableLiveData<Boolean>()

	override fun <T> requestWithSession(f: () -> Single<T>): Single<T> {
		return f() // TODO test if request is failed due to invalid session
	}
}
