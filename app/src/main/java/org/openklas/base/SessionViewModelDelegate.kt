package org.openklas.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single

interface SessionViewModelDelegate {
	val mustAuthenticate: MutableLiveData<Boolean>

	fun <T> requestWithSession(f: () -> Single<T>): Single<T>
}
