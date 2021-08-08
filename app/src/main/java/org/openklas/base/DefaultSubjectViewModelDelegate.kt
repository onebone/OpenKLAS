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

package org.openklas.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import org.openklas.klas.model.BriefSubject
import javax.inject.Inject

class DefaultSubjectViewModelDelegate @Inject constructor(
	semesterViewModelDelegate: SemesterViewModelDelegate
): SubjectViewModelDelegate, SemesterViewModelDelegate by semesterViewModelDelegate {
	private val _currentSubject = MediatorLiveData<BriefSubject>().apply {
		addSource(subjects) {
			invokeSelector(it)
		}
	}
	override val currentSubject: LiveData<BriefSubject> = _currentSubject

	private var subjectSelector: (List<BriefSubject>) -> BriefSubject? = {
		it.firstOrNull()
	}

	override fun setSubject(subjectId: String) {
		subjectSelector = { subjects ->
			subjects.find { it.id == subjectId }
		}

		subjects.value?.let {
			invokeSelector(it)
		}
	}

	private fun invokeSelector(subjects: List<BriefSubject>) {
		val selection = subjectSelector(subjects) ?: return

		if(selection != currentSubject.value) {
			_currentSubject.value = selection
		}
	}
}
