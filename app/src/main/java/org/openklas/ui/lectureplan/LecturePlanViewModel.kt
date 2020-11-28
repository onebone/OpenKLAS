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

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.fragment.NavHostFragment
import com.github.windsekirun.daggerautoinject.InjectViewModel
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.event.Event
import org.openklas.klas.model.Board
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.repository.KlasRepository
import org.openklas.ui.home.HomeFragment
import javax.inject.Inject

@InjectViewModel
class LecturePlanViewModel @Inject constructor(
	app: MainApplication,
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(app), SessionViewModelDelegate by sessionViewModelDelegate {
	// semester must be set externally, others are optional
	init{

	}

	fun clickBack(view : View){
		requireActivity().onBackPressed()
	}

	fun clickSearch(view: View){
		postEvent(Event(true))
	}
}
