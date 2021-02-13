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

	private val _syllabusList = MutableLiveData<Array<SyllabusSummary>>()
	val syllabusList: LiveData<Array<SyllabusSummary>> = _syllabusList

	private val _filter = MutableLiveData<Filter>()
	val filter: LiveData<Filter> = _filter

	fun setFilter(year: Int, term: Int, keyword: String, professor: String) {
		_filter.value = Filter(year, term, keyword, professor)

		fetchSyllabus(year, term,  keyword, professor)
	}

	private fun fetchSyllabus(year: Int, term: Int, keyword: String, professor: String) {
		addDisposable(requestWithSession {
			klasRepository.getSyllabusList(year, term, keyword, professor)
		}.subscribe { v, err ->
			if (err == null) {
				_syllabusList.value = v
			} else {
				_error.value = err
			}
		})
	}

	data class Filter(
		val year: Int,
		val term: Int,
		val keyword: String,
		val professor: String
	)
}
