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

package org.openklas.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Semester
import org.openklas.klas.model.Timetable
import org.openklas.repository.KlasRepository
import org.openklas.utils.Result
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate {

	private val home = MediatorLiveData<Home>().apply {
		addSource(currentSemester) {
			fetchHome(it.id)
		}
	}

	private val onlineContents = MediatorLiveData<Array<Pair<BriefSubject, OnlineContentEntry>>>().apply {
		addSource(currentSemester) {
			fetchOnlineContents(it)
		}
	}

	val videos: LiveData<List<Pair<BriefSubject, OnlineContentEntry.Video>>> = Transformations.map(onlineContents) {
		val now = Date()

		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Video && entry.progress < 100 && entry.startDate < now && now < entry.dueDate
		}.sortedBy { (_, entry) -> entry.dueDate.time } as List<Pair<BriefSubject, OnlineContentEntry.Video>>
	}

	val impendingVideo: LiveData<List<Pair<BriefSubject, OnlineContentEntry.Video>>> = Transformations.map(onlineContents) {
		val now = Date()

		@Suppress("UNCHECKED_CAST")
		(it.filter { (_, entry) ->
			entry is OnlineContentEntry.Video && entry.progress < 100 && isImpending(entry.dueDate.time - now.time)
		} as List<Pair<BriefSubject, OnlineContentEntry.Video>>)
	}

	val homeworks: LiveData<List<Pair<BriefSubject, OnlineContentEntry.Homework>>> = Transformations.map(onlineContents) {
		val now = Date()

		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && entry.startDate < now && now < entry.dueDate
		}.sortedBy { (_, entry: OnlineContentEntry) -> entry.dueDate.time } as List<Pair<BriefSubject, OnlineContentEntry.Homework>>
	}

	val impendingHomework: LiveData<List<Pair<BriefSubject, OnlineContentEntry.Homework>>> = Transformations.map(onlineContents) {
		val now = Date()

		@Suppress("UNCHECKED_CAST")
		(it.filter { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && isImpending(entry.dueDate.time - now.time)
		} as List<Pair<BriefSubject, OnlineContentEntry.Homework>>)
	}

	val quiz: LiveData<List<Pair<BriefSubject, OnlineContentEntry.Quiz>>> = Transformations.map(onlineContents) {
		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Quiz
		} as List<Pair<BriefSubject, OnlineContentEntry.Quiz>>
	}

	val timetable: LiveData<Timetable>  = Transformations.map(home) {
		it.timetable
	}
	val todaySchedule: LiveData<List<Timetable.Entry>> = Transformations.map(timetable) { timetable ->
		val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
		timetable.entries.filter{ it.day == day - 1 }.sortedBy { it.time }
	}

	private val _isHomeFetched = MutableLiveData(false)
	val isHomeFetched: LiveData<Boolean> = _isHomeFetched

	private val _isOnlineContentsFetched = MutableLiveData(false)
	val isOnlineContentsFetched: LiveData<Boolean> = _isOnlineContentsFetched

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private fun isImpending(time: Long): Boolean {
		return 0 < time && time < TimeUnit.HOURS.toMillis(24)
	}

	private fun fetchHome(semester: String) {
		_isHomeFetched.value = false

		viewModelScope.launch {
			val result = requestWithSession {
				klasRepository.getHome(semester)
			}

			when (result) {
				is Result.Success -> home.postValue(result.value)
				is Result.Error -> _error.postValue(result.error)
			}

			_isHomeFetched.postValue(true)
		}
	}

	private fun fetchOnlineContents(currentSemester: Semester) {
		_isOnlineContentsFetched.value = false

		viewModelScope.launch {
			try {
				coroutineScope {
					val contents = currentSemester.subjects.map { subject ->
						async {
							val result = requestWithSession {
								klasRepository.getOnlineContentList(currentSemester.id, subject.id)
							}

							if(result is Result.Error) {
								throw result.error
							}

							(result as Result.Success).value.map {
								Pair(subject, it)
							}
						}
					}.flatMap {
						it.await()
					}.toTypedArray()

					onlineContents.postValue(contents)
				}
			}catch(e: Throwable) {
				_error.postValue(e)
			}finally{
				_isOnlineContentsFetched.postValue(true)
			}
		}
	}
}
