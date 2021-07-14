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

package org.openklas.ui.grade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.repository.KlasRepository
import org.openklas.utils.ViewResource
import org.openklas.utils.sharedFlow
import org.openklas.utils.transform
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
	private val retryTrigger = Channel<Unit>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

	val grades = sharedFlow(retryTrigger, viewModelScope) {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getGrades()
		}.transform())
	}

	val schoolRegister = sharedFlow(retryTrigger, viewModelScope) {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getSchoolRegister()
		}.transform())
	}

	val creditStatus = sharedFlow(retryTrigger, viewModelScope) {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getCreditStatus()
		}.transform())
	}

	fun retry() {
		retryTrigger.trySend(Unit)
	}
}
