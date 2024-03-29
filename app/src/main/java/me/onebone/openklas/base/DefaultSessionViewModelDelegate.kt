/*
 * OpenKLAS
 * Copyright (C) 2020-2021 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.onebone.openklas.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.onebone.openklas.klas.error.KlasSessionInvalidError
import me.onebone.openklas.repository.SessionRepository
import me.onebone.openklas.utils.Event
import me.onebone.openklas.utils.Resource
import javax.inject.Inject

class DefaultSessionViewModelDelegate @Inject constructor(
	private val sessionRepository: SessionRepository
): SessionViewModelDelegate {
	companion object {
		const val TAG = "SessionViewModel"
	}

	//private val _mustAuthenticate = MutableLiveData<Event<Unit>>()
	override val mustAuthenticate = MutableLiveData<Event<Unit>>()

	override suspend fun <T> requestWithSession(f: suspend () -> Resource<T>): Resource<T> = withContext(Dispatchers.IO) {
		try {
			val result = f()
			if(result is Resource.Error) {
				val error = result.error

				Log.e(TAG, "requestWithSession", error)
				if(error is KlasSessionInvalidError) {
					// try logging in automatically if session is invalid
					// and send original request one more time
					return@withContext if(sessionRepository.tryLogin()) {
						f()
					}else{
						mustAuthenticate.postValue(Event(Unit))
						Resource.Error(KlasSessionInvalidError())
					}
				}

				// if an error is other than invalid session, just return the original one
			}

			result
		}catch(e: Throwable) {
			// FIXME correct?
			Firebase.crashlytics.recordException(e)

			Log.e(TAG, "requestWithSession", e)
			Resource.Error(e)
		}
	}
}
