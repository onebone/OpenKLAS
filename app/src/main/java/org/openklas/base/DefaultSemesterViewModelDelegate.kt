package org.openklas.base

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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import org.openklas.klas.model.Semester
import org.openklas.repository.KlasRepository
import org.openklas.utils.Result
import javax.inject.Inject

class DefaultSemesterViewModelDelegate @Inject constructor(
	private val klasRepository: KlasRepository
): SemesterViewModelDelegate {
	override val semesters = liveData {
		val result = klasRepository.getSemesters()

		if(result is Result.Success) {
			emit(result.value)
		}
		// TODO forward error
	}

	private val _currentSemester = MutableLiveData<Semester>()
	override val currentSemester = Transformations.switchMap(semesters) {
		// TODO set default semester according to current time
		// if the user has enrolled in winter or summer session,
		// default semester will be set to it even if a fall or
		// spring session is not finished.

		it.firstOrNull()?.let { first ->
			_currentSemester.value = first
		}
		_currentSemester
	}

	override val subjects = Transformations.map(currentSemester) {
		it.subjects
	}

	override fun setCurrentSemester(semester: String) {
		semesters.value?.find { it.id == semester }?.let {
			_currentSemester.value = it
		}
	}
}
