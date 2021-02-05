package org.openklas.ui.sylsearch

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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.SyllabusSummary
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@HiltViewModel
class SylSearchViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
) : BaseViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
	private val _error = MutableLiveData<Throwable>()

	val keyword = MutableLiveData<String>()

	private val _syllabusList = MediatorLiveData<Array<SyllabusSummary>>().apply {
		addSource(keyword) {
			fetchSyllabus(it)
		}
	}
	val syllabusList: LiveData<Array<SyllabusSummary>> = _syllabusList

	private fun fetchSyllabus(keyword: String) {
		// TODO configure semester
		addDisposable(requestWithSession {
			klasRepository.getSyllabusList(2020, 2, keyword, "")
		}.subscribe { v, err ->
			if (err == null) {
				_syllabusList.value = v
			} else {
				_error.value = err
			}
		})
	}
}
