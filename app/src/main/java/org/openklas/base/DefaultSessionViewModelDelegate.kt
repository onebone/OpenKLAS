package org.openklas.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import org.openklas.repository.SessionRepository
import javax.inject.Inject

class DefaultSessionViewModelDelegate @Inject constructor(
	private val sessionRepository: SessionRepository
): SessionViewModelDelegate {
	override val mustAuthenticate = MutableLiveData<Boolean>()

	override fun <T> requestWithSession(f: () -> Single<T>): Single<T> {
		return f()
			.onErrorResumeNext { err ->
				// TODO check if [err] is session-related error
				sessionRepository.tryLogin()
					.flatMap {
						if(it) {
							f()
						}else{
							mustAuthenticate.value = true
							Single.error<T>(err)
						}
					}
			}
	}
}
