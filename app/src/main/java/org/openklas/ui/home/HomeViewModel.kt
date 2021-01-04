package org.openklas.ui.home

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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Single
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Semester
import org.openklas.klas.model.Timetable
import org.openklas.repository.KlasRepository
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeViewModel @ViewModelInject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(), SessionViewModelDelegate by sessionViewModelDelegate {
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

	private val onlineContents = MediatorLiveData<Array<Pair<BriefSubject, OnlineContentEntry>>>().apply {
		addSource(semester) {
			fetchOnlineContents(it)
		}
	}

	val videoCount: LiveData<Int> = Transformations.map(onlineContents) {
		val now = Date()

		it.count { (_, entry) ->
			entry is OnlineContentEntry.Video && entry.progress < 100 && entry.startDate < now && now < entry.endDate
		}
	}

	val homeworkCount: LiveData<Int> = Transformations.map(onlineContents) {
		val now = Date()

		it.count { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && entry.startDate < now && now < entry.endDate
		}
	}

	val impendingHomework: LiveData<Array<Pair<BriefSubject, OnlineContentEntry.Homework>>> = Transformations.map(onlineContents) {
		val now = Date()

		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && now.time - entry.endDate.time < TimeUnit.HOURS.toMillis(24)
		}.toTypedArray() as Array<Pair<BriefSubject, OnlineContentEntry.Homework>>
	}
	val impendingHomeworkCount: LiveData<Int> = Transformations.map(impendingHomework) {
		it.size
	}

	val semesterLabel: LiveData<String> = Transformations.map(home) {
		it.semesterLabel
	}

	val subjectCount: LiveData<Int> = Transformations.map(home) {
		it.subjects.size
	}

	val timetable: LiveData<Timetable>  = Transformations.map(home) {
		it.timetable
	}
	val todaySchedule: LiveData<Array<Timetable.Entry>> = Transformations.map(timetable) { timetable ->
		val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
		timetable.entries.filter{ it.day == day - 1 }.sortedBy { it.time }.toTypedArray()
	}
	val todayScheduleEmpty: LiveData<Boolean> = Transformations.map(todaySchedule) { it.isEmpty() }

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private fun fetchSemesters() {
		addDisposable(requestWithSession {
			klasRepository.getSemesters()
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

	private fun fetchOnlineContents(currentSemester: Semester) {
		addDisposable(Single.zip(currentSemester.subjects.map { subject ->
			requestWithSession {
				klasRepository.getOnlineContentList(currentSemester.id, subject.id).map { entries ->
					entries.map { Pair(subject, it) }
				}
			}
		}) {
			it.flatMap { entry ->
				@Suppress("UNCHECKED_CAST")
				entry as List<Pair<BriefSubject, OnlineContentEntry>>
			}.toTypedArray()
		}.subscribe { v, err ->
			if(err == null) {
				onlineContents.value = v
			}else{
				_error.value = err
			}
		})
	}
}
