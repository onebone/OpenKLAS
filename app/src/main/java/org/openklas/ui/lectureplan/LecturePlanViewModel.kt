package org.openklas.ui.lectureplan

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

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.windsekirun.daggerautoinject.InjectViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.event.Event
import org.openklas.klas.model.SyllabusSummary
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@InjectViewModel
class LecturePlanViewModel @Inject constructor(
	app: MainApplication,
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
) : BaseViewModel(app), SessionViewModelDelegate by sessionViewModelDelegate {

	init {
	}

	private val _error = MutableLiveData<Throwable>()
	val mListKeyword = ObservableArrayList<SyllabusSummary>()


	fun getList(keyword: String) {

		addDisposable(requestWithSession {
			klasRepository.getSyllabusList(2020, 2, keyword, "")
		}.subscribe { v, err ->
			if (err == null) {
				mListKeyword.clear()
				mListKeyword.addAll(v)
			} else {
				_error.value = err
			}
		})
	}

	fun clickBack(view: View) {
		requireActivity().onBackPressed()
	}

	fun clickSearch(view: View) {
		postEvent(Event(true))
	}
}
