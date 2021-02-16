package org.openklas.ui.syllabus

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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.TeachingAssistant
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@HiltViewModel
class SyllabusViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
	private val _syllabus = MutableLiveData<Syllabus>()
	val syllabus: LiveData<Syllabus> = _syllabus

	private val _teachingAssistants = MutableLiveData<Array<TeachingAssistant>>()
	val teachingAssistants: LiveData<Array<TeachingAssistant>> = _teachingAssistants

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	fun fetchSyllabus(subjectId: String) {
		addDisposable(requestWithSession {
			klasRepository.getSyllabus(subjectId)
		}.subscribe { v, err ->
			if(err != null) {
				_error.value = err
			}else{
				fetchTeachingAssistants(subjectId)
				_syllabus.value = v
			}
		})
	}

	private fun fetchTeachingAssistants(subjectId: String) {
		addDisposable(requestWithSession {
			klasRepository.getTeachingAssistants(subjectId)
		}.subscribe { v, err ->
			if(err != null) {
				_error.value = err
			}else{
				_teachingAssistants.value = v
			}
		})
	}
}
