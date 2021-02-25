package org.openklas.base

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

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openklas.klas.error.KlasSessionInvalidError
import org.openklas.repository.SessionRepository
import org.openklas.utils.Event
import org.openklas.utils.Result
import javax.inject.Inject

class DefaultSessionViewModelDelegate @Inject constructor(
	private val sessionRepository: SessionRepository
): SessionViewModelDelegate {
	companion object {
		const val TAG = "SessionViewModel"
	}

	//private val _mustAuthenticate = MutableLiveData<Event<Unit>>()
	override val mustAuthenticate = MutableLiveData<Event<Unit>>()

	override suspend fun <T> requestWithSession(f: suspend () -> Result<T>): Result<T> = withContext(Dispatchers.IO) {
		try {
			val result = f()
			if(result is Result.Error) {
				val error = result.error

				Log.e(TAG, "requestWithSession", error)
				if(error is KlasSessionInvalidError) {
					// try logging in automatically if session is invalid
					// and send original request one more time
					return@withContext if(sessionRepository.tryLogin()) {
						f()
					}else{
						mustAuthenticate.postValue(Event(Unit))
						Result.Error(KlasSessionInvalidError())
					}
				}

				// if an error is other than invalid session, just return the original one
			}

			result
		}catch(e: Throwable) {
			Result.Error(e)
		}
	}
}
