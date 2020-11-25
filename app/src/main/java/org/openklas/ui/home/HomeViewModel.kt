package org.openklas.ui.home

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.github.windsekirun.daggerautoinject.InjectViewModel
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@InjectViewModel
class HomeViewModel @Inject constructor(
	app: MainApplication,
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(app), SessionViewModelDelegate by sessionViewModelDelegate {
	init {
		fetchSemesters()
	}

	private val _semesters = MutableLiveData<Array<Semester>>()
	val semesters: LiveData<Array<Semester>> = _semesters

	private val _semester = MutableLiveData<Semester>()
	val semester: LiveData<Semester> = _semester

	private val home = MediatorLiveData<Home>().apply {
		addSource(semester) {
			fetchHome(it.id)
		}
	}

	val semesterLabel: LiveData<String> = Transformations.map(home) {
		it.semesterLabel
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private fun fetchSemesters() {
		addDisposable(requestWithSession {
			klasRepository.semesters
		}.subscribe { v, err ->
			if(err == null) {
				_semesters.value = v

				if(_semester.value == null && v.isNotEmpty()) {
					// TODO set default semester according to current time
					// if the user has enrolled in winter or summer session,
					// default semester will be set to it even if a fall or
					// spring session is not finished.
					_semester.value = v[0]
				}
			}else{
				_error.value = err
			}
		})
	}

	private fun fetchHome(semester: String) {
		addDisposable(requestWithSession {
			klasRepository.getHome(semester)
		}.subscribe { v, err ->
			if(err == null) {
				home.value = v
			}else{
				_error.value = err
			}
		})
	}

	fun clickBtn(view: View){
		showToast("클릭됨")
	}
}
