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

package me.onebone.openklas.ui.assignment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.onebone.openklas.base.ErrorViewModelDelegate
import me.onebone.openklas.base.SessionViewModelDelegate
import me.onebone.openklas.base.SubjectViewModelDelegate
import me.onebone.openklas.klas.model.Assignment
import me.onebone.openklas.klas.model.Attachment
import me.onebone.openklas.repository.KlasRepository
import me.onebone.openklas.utils.Resource

@HiltViewModel
class AssignmentViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	subjectViewModelDelegate: SubjectViewModelDelegate,
	errorViewModelDelegate: ErrorViewModelDelegate
): ViewModel(),
	SessionViewModelDelegate by sessionViewModelDelegate,
	SubjectViewModelDelegate by subjectViewModelDelegate,
	ErrorViewModelDelegate by errorViewModelDelegate {

	companion object {
		const val STORAGE_ID = "CLS_PROF_TASK"
	}

	private val _assignment = MutableLiveData<Assignment>()
	val assignment: LiveData<Assignment> = _assignment

	val attachments = assignment.switchMap {
		MutableLiveData<List<Attachment>>().apply {
			if(it.description.attachmentId == null) {
				value = emptyList()
				return@apply
			}

			viewModelScope.launch {
				val result = requestWithSession {
					klasRepository.getAttachments(STORAGE_ID, it.description.attachmentId)
				}

				when(result) {
					is Resource.Success -> postValue(result.value)
					is Resource.Error -> emitError(result.error)
				}
			}
		}
	}

	fun fetchAssignment(semester: String, subject: String, order: Int) {
		setSubject(subject)
		setCurrentSemester(semester)

		viewModelScope.launch {
			val result = requestWithSession {
				klasRepository.getAssignment(semester, subject, order)
			}

			@SuppressLint("NullSafeMutableLiveData")
			when(result) {
				is Resource.Success -> _assignment.postValue(result.value)
				is Resource.Error -> emitError(result.error)
			}
		}
	}
}
