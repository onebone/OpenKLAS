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

package org.openklas.ui.login

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openklas.repository.KlasRepository
import org.openklas.repository.SessionRepository
import org.openklas.utils.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	private val sessionRepository: SessionRepository
): ViewModel() {
	val userId = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	val rememberMe = ObservableBoolean(true)

	private val _result = MutableLiveData<Throwable?>()
	// value of result will be set whenever the login task if succeed or failed.
	// The value is null if succeed, or Throwable instance otherwise.
	val result: LiveData<Throwable?> = _result

	init {
		sessionRepository.getSavedCredential()?.let { credential ->
			userId.value = credential.username
			password.value = credential.password
		}
	}

	fun handleLogin() {
		val userId = userId.value?.trim() ?: ""
		val password = password.value?.trim() ?: ""

		if(userId == "" || password == "") {
			_result.value = AuthFieldEmptyException()
			return
		}

		viewModelScope.launch {
			val result = klasRepository.performLogin(userId, password, rememberMe.get())
			_result.value = (result as? Resource.Error)?.error
		}
	}

	fun checkSavedSession() {
		viewModelScope.launch {
			if(sessionRepository.testSession()) {
				_result.value = null
			}
		}
	}

	class AuthFieldEmptyException: Throwable()
}
