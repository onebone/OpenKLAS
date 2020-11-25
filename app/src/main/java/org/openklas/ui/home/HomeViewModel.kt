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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Home
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@InjectViewModel
class HomeViewModel @Inject constructor(
	app: MainApplication,
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(app), SessionViewModelDelegate by sessionViewModelDelegate {
	val semester = MutableLiveData<String>()

	private val home = MediatorLiveData<Home>().apply {
		addSource(semester) {
			fetchHome(it)
		}
	}

	val semesterLabel: LiveData<String> = Transformations.map(home) {
		it.semesterLabel
	}

	val error = MutableLiveData<Throwable>()

	private fun fetchHome(semester: String) {
		addDisposable(requestWithSession {
			klasRepository.getHome(semester)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
		}.subscribe { v, err ->
			if(err == null) {
				home.value = v
			}else{
				error.value = err
			}
		})
	}

	fun clickBtn(view: View){
		showToast("클릭됨")
	}
}
