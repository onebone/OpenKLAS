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

package org.openklas.ui.syllabus

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.LectureSchedule
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.TeachingAssistant
import org.openklas.repository.KlasRepository
import org.openklas.ui.syllabus.page.summary.TUTOR_PROFESSOR
import org.openklas.ui.syllabus.page.summary.TUTOR_TEACHING_ASSISTANT
import org.openklas.ui.syllabus.page.summary.TutorEntry
import org.openklas.utils.Resource
import javax.inject.Inject

@HiltViewModel
class SyllabusViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
	private val _syllabus = MutableLiveData<Syllabus>()
	val syllabus: LiveData<Syllabus> = _syllabus

	private val _teachingAssistants = MutableLiveData<Array<TeachingAssistant>>()

	private val _schedules = MutableLiveData<Array<LectureSchedule>>()
	val schedules: LiveData<Array<LectureSchedule>> = _schedules

	private val _studentsNumber = MutableLiveData<Int>()
	val studentsNumber: LiveData<Int> = _studentsNumber

	val tutors: LiveData<Array<TutorEntry>> = MediatorLiveData<Array<TutorEntry>>().apply {
		fun combine() {
			val tutors = mutableListOf<TutorEntry>()

			val tutor = syllabus.value?.tutor
			if(tutor != null) {
				tutors += TutorEntry(tutor.name, TUTOR_PROFESSOR, tutor.email, tutor.contact, tutor.telephoneContact)
			}

			val teachingAssistants = _teachingAssistants.value
			if(teachingAssistants != null) {
				tutors += teachingAssistants.map {
					TutorEntry(it.name, TUTOR_TEACHING_ASSISTANT, it.email, null, null)
				}
			}

			value = tutors.toTypedArray()
		}

		addSource(_teachingAssistants) {
			combine()
		}

		addSource(syllabus) {
			combine()
		}
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private suspend fun fetchTeachingAssistants(subjectId: String) {
		val result = requestWithSession {
			klasRepository.getTeachingAssistants(subjectId)
		}

		@SuppressLint("NullSafeMutableLiveData")
		when(result) {
			is Resource.Success -> _teachingAssistants.postValue(result.value)
			is Resource.Error -> _error.postValue(result.error)
		}
	}

	fun fetchSyllabus(subjectId: String) {
		viewModelScope.launch {
			launch {
				fetchTeachingAssistants(subjectId)
			}

			launch {
				fetchLectureSchedules(subjectId)
			}

			launch {
				fetchLectureStudentsNumber(subjectId)
			}

			val result = requestWithSession {
				klasRepository.getSyllabus(subjectId)
			}

			@SuppressLint("NullSafeMutableLiveData")
			when(result) {
				is Resource.Success -> _syllabus.postValue(result.value)
				is Resource.Error -> _error.postValue(result.error)
			}
		}
	}

	private suspend fun fetchLectureSchedules(subjectId: String) {
		val result = requestWithSession {
			klasRepository.getLectureSchedules(subjectId)
		}

		@SuppressLint("NullSafeMutableLiveData")
		when(result) {
			is Resource.Success -> _schedules.postValue(result.value)
			is Resource.Error -> _error.postValue(result.error)
		}
	}

	private suspend fun fetchLectureStudentsNumber(subjectId: String) {
		val result = requestWithSession {
			klasRepository.getLectureStudentsNumber(subjectId)
		}

		@SuppressLint("NullSafeMutableLiveData")
		when(result) {
			is Resource.Success -> _studentsNumber.postValue(result.value)
			is Resource.Error -> _error.postValue(result.error)
		}
	}
}
