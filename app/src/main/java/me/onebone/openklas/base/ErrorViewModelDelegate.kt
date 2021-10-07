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

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import me.onebone.openklas.utils.SharedEventFlow

/**
 * Used to delegate error handling to root activity.
 */
interface ErrorViewModelDelegate {
	val error: Flow<Throwable>

	fun emitError(throwable: Throwable)
}

class ErrorViewModelDelegateImpl @Inject constructor(): ErrorViewModelDelegate {
	private val _error = SharedEventFlow<Throwable>()
	override val error: Flow<Throwable> = _error

	override fun emitError(throwable: Throwable) {
		// TODO use injection instead
		Firebase.crashlytics.recordException(throwable)
		_error.tryEmit(throwable)
	}
}
