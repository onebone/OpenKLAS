package org.openklas.base

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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
