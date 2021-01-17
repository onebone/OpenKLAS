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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.Disposable
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.repository.KlasRepository
import javax.inject.Inject

class DefaultSemesterViewModelDelegate @Inject constructor(
	private val klasRepository: KlasRepository
): SemesterViewModelDelegate {
	private val _semesters = MutableLiveData<Array<Semester>>()
	override val semesters: LiveData<Array<Semester>> by lazy {
		fetchSemesters()
		_semesters
	}

	private val _currentSemester = MutableLiveData<Semester>()
	override val currentSemester: LiveData<Semester> by lazy {
		// TODO set default semester according to current date
		semesters.value?.firstOrNull()?.let {
			_currentSemester.value = it
		}
		_currentSemester
	}

	private var fetchDisposable: Disposable? = null

	override val subjects: LiveData<Array<BriefSubject>> by lazy {
		Transformations.map(currentSemester) {
			it.subjects
		}
	}

	override fun setCurrentSemester(semester: String) {
		semesters.value?.find { it.id == semester }?.let {
			_currentSemester.value = it
		}
	}

	private fun fetchSemesters() {
		fetchDisposable?.dispose()

		fetchDisposable = klasRepository.getSemesters()
			.subscribe { v, err ->
				if(err == null) {
					_semesters.value = v

					if(_currentSemester.value == null) {
						// TODO set default semester according to current time
						// if the user has enrolled in winter or summer session,
						// default semester will be set to it even if a fall or
						// spring session is not finished.

						if(v.isNotEmpty()) {
							_currentSemester.value = v[0]
						}
					}
				}else{
					// TODO forward error
				}
			}
	}
}
