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

package org.openklas.ui.assignmentlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openklas.base.SessionViewModelDelegate
import org.openklas.base.SubjectViewModelDelegate
import org.openklas.klas.model.AssignmentEntry
import org.openklas.repository.KlasRepository
import org.openklas.utils.PairCombinedLiveData
import org.openklas.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AssignmentListViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	subjectViewModelDelegate: SubjectViewModelDelegate
): ViewModel(),
	SessionViewModelDelegate by sessionViewModelDelegate,
	SubjectViewModelDelegate by subjectViewModelDelegate {

	// though currentSemester should not be null when subject is non-null,
	// we will make it clear
	private val semesterSubjectPair = PairCombinedLiveData(currentSemester, currentSubject)

	private val _assignments = MediatorLiveData<List<AssignmentEntry>>().apply {
		addSource(semesterSubjectPair) {
			fetchAssignments(it.first.id, it.second.id)
		}
	}
	val assignments: LiveData<List<AssignmentEntry>> = _assignments

	private val _isLoading = MutableLiveData(true)
	val isLoading: LiveData<Boolean> = _isLoading

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private fun fetchAssignments(semester: String, subjectId: String) {
		viewModelScope.launch {
			_isLoading.postValue(true)

			val result = requestWithSession {
				klasRepository.getAssignments(semester, subjectId)
			}

			_isLoading.postValue(false)

			when(result) {
				is Resource.Success -> _assignments.postValue(result.value)
				is Resource.Error -> _error.postValue(result.error)
			}
		}
	}
}
