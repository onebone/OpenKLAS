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

import android.annotation.SuppressLint
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import javax.inject.Inject
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.repository.KlasRepository
import me.onebone.openklas.utils.Resource

class DefaultSemesterViewModelDelegate @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): SemesterViewModelDelegate, SessionViewModelDelegate by sessionViewModelDelegate {
	private var semesterSelector: (List<Semester>) -> Semester? = {
		// TODO set default semester according to current time
		// if the user has enrolled in winter or summer session,
		// default semester will be set to it even if a fall or
		// spring session is not finished.

		it.firstOrNull()
	}

	override val semesters = liveData {
		val result = requestWithSession { klasRepository.getSemesters() }

		if(result is Resource.Success) {
			emit(result.value)
		}
		// TODO forward error
	}

	private val _currentSemester = MutableLiveData<Semester>()
	override val currentSemester = MediatorLiveData<Semester>().apply {
		addSource(semesters) {
			invokeSelector(it)
		}

		addSource(_currentSemester) {
			value = it
		}
	}

	override val subjects = currentSemester.map {
		it.subjects
	}

	override fun setCurrentSemester(semester: String) {
		semesterSelector = { semesters ->
			semesters.find { it.id == semester }
		}

		semesters.value?.let {
			invokeSelector(it)
		}
	}

	private fun invokeSelector(semesters: List<Semester>) {
		val semester = semesterSelector(semesters)
		if(semester == null && semesters.isNotEmpty()) return

		@SuppressLint("NullSafeMutableLiveData")
		if(_currentSemester.value != semester && semester != null)
			_currentSemester.value = semester
	}
}
