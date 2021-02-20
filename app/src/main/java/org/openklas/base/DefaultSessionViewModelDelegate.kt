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
import io.reactivex.Single
import org.openklas.klas.error.KlasSessionInvalidError
import org.openklas.repository.SessionRepository
import org.openklas.utils.Event
import javax.inject.Inject

class DefaultSessionViewModelDelegate @Inject constructor(
	private val sessionRepository: SessionRepository
): SessionViewModelDelegate {
	companion object {
		const val TAG = "SessionViewModel"
	}

	override val mustAuthenticate = MutableLiveData<Event<Unit>>()

	override fun <T> requestWithSession(f: () -> Single<T>): Single<T> {
		return f().onErrorResumeNext { err ->
			Log.e(TAG, "requestWithSession:", err)

			if(err is KlasSessionInvalidError) {
				// try logging in automatically if session is invalid
				// and send original request one more time
				sessionRepository.tryLogin().flatMap {
					if(it) {
						f()
					}else{
						mustAuthenticate.value = Event(Unit)
						Single.error(err)
					}
				}
			}else{
				Single.error(err)
			}
		}
	}
}
