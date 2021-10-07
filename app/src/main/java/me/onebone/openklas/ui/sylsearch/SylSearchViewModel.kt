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

package me.onebone.openklas.ui.sylsearch

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.onebone.openklas.base.SessionViewModelDelegate
import me.onebone.openklas.klas.model.SyllabusSummary
import me.onebone.openklas.repository.KlasRepository
import me.onebone.openklas.utils.Resource
import javax.inject.Inject
import me.onebone.openklas.base.ErrorViewModelDelegate

@HiltViewModel
class SylSearchViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	errorViewModelDelegate: ErrorViewModelDelegate
) : ViewModel(),
	SessionViewModelDelegate by sessionViewModelDelegate,
	ErrorViewModelDelegate by errorViewModelDelegate {

	private val _syllabusList = MutableLiveData<List<SyllabusSummary>>()
	val syllabusList: LiveData<List<SyllabusSummary>> = _syllabusList

	private val _filter = MutableLiveData<Filter>()
	val filter: LiveData<Filter> = _filter

	fun setFilter(year: Int, term: Int, keyword: String, professor: String) {
		_filter.value = Filter(year, term, keyword, professor)

		fetchSyllabus(year, term,  keyword, professor)
	}

	private fun fetchSyllabus(year: Int, term: Int, keyword: String, professor: String) {
		viewModelScope.launch {
			val result = requestWithSession {
				klasRepository.getSyllabusList(year, term, keyword, professor)
			}

			@SuppressLint("NullSafeMutableLiveData")
			when(result) {
				is Resource.Success -> _syllabusList.postValue(result.value)
				is Resource.Error -> emitError(result.error)
			}
		}
	}

	data class Filter(
		val year: Int,
		val term: Int,
		val keyword: String,
		val professor: String
	)
}
