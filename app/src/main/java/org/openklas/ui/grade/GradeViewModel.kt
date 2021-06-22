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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import org.openklas.base.SessionViewModelDelegate
import org.openklas.repository.KlasRepository
import org.openklas.utils.ViewResource
import org.openklas.utils.transform
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
	val grades = flow {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getGrades()
		}.transform())
	}.shareIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(),
		replay = 1
	)

	val schoolRegister = flow {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getSchoolRegister()
		}.transform())
	}.shareIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(),
		replay = 1
	)

	val creditStatus = flow {
		emit(ViewResource.Loading())

		emit(requestWithSession {
			klasRepository.getCreditStatus()
		}.transform())
	}.shareIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(),
		replay = 1
	)
}
