package org.openklas.ui.login

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

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.openklas.base.BaseViewModel
import org.openklas.repository.KlasRepository

class LoginViewModel @ViewModelInject constructor(
	private val klasRepository: KlasRepository
): BaseViewModel() {
	val userId = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	val rememberMe = ObservableBoolean(true)

	private val _result = MutableLiveData<Throwable?>()
	// value of result will be set whenever the login task if succeed or failed.
	// The value is null if succeed, or Throwable instance otherwise.
	val result: LiveData<Throwable?> = _result

	fun handleLogin() {
		val userId = userId.value?.trim() ?: ""
		val password = password.value?.trim() ?: ""

		if(userId == "" || password == "") {
			_result.value = AuthFieldEmptyException()
			return
		}

		addDisposable(klasRepository.performLogin(userId, password, rememberMe.get())
			.subscribe { _, err ->
				_result.value = err
			})
	}

	class AuthFieldEmptyException: Throwable()
}
