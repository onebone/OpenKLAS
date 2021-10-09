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

package me.onebone.openklas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import me.onebone.openklas.base.SemesterViewModelDelegate
import me.onebone.openklas.base.SessionViewModelDelegate
import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.OnlineContentEntry
import me.onebone.openklas.repository.KlasRepository
import me.onebone.openklas.utils.Resource
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.onebone.openklas.base.ErrorViewModelDelegate
import me.onebone.openklas.base.FlowRegistrar
import me.onebone.openklas.base.KeyedScope
import me.onebone.openklas.utils.ViewResource
import me.onebone.openklas.utils.mapResource
import me.onebone.openklas.utils.transform

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate,
	errorViewModelDelegate: ErrorViewModelDelegate,
	registrar: FlowRegistrar<Any>
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate,
	ErrorViewModelDelegate by errorViewModelDelegate,
	KeyedScope<HomeViewModel.Key> by registrar {

	enum class Key {
		Home, OnlineContents;
	}

	private val home = registrar.register(Key.Home) {
		currentSemester
			.asFlow()
			.map {
				val result = requestWithSession {
					klasRepository.getHome(it.id)
				}

				result.transform()
			}
	}

	private val onlineContents = registrar.register(Key.OnlineContents) {
		currentSemester
			.asFlow()
			.map {
				try {
					ViewResource.Success(coroutineScope {
						it.subjects.map { subject ->
							async {
								val result = requestWithSession {
									klasRepository.getOnlineContentList(it.id, subject.id)
								}

								if(result is Resource.Error) {
									throw result.error
								}

								(result as Resource.Success).value.map {
									Pair(subject, it)
								}
							}
						}.flatMap {
							it.await()
						}
					})
				}catch(e: Throwable) {
					ViewResource.Error(e)
				}
			}
	}

	val videos = onlineContents.mapResource {
		val now = ZonedDateTime.now()

		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Video && entry.progress < 100 && entry.startDate < now && now < entry.dueDate
		}.sortedBy { (_, entry) -> entry.dueDate.nano } as List<Pair<BriefSubject, OnlineContentEntry.Video>>
	}

	val impendingVideo = onlineContents.mapResource {
		val now = ZonedDateTime.now()

		@Suppress("UNCHECKED_CAST")
		(it.filter { (_, entry) ->
			entry is OnlineContentEntry.Video && entry.progress < 100 && isImpending(entry.dueDate.nano - now.nano)
		} as List<Pair<BriefSubject, OnlineContentEntry.Video>>)
	}

	val homeworks = onlineContents.mapResource {
		throw RuntimeException("hello world")
		val now = ZonedDateTime.now()

		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && entry.startDate < now && now < entry.dueDate
		}.sortedBy { (_, entry: OnlineContentEntry) -> entry.dueDate.nano } as List<Pair<BriefSubject, OnlineContentEntry.Homework>>
	}

	val impendingHomework = onlineContents.mapResource {
		val now = ZonedDateTime.now()

		@Suppress("UNCHECKED_CAST")
		(it.filter { (_, entry) ->
			entry is OnlineContentEntry.Homework && entry.submitDate == null && isImpending(entry.dueDate.nano - now.nano)
		} as List<Pair<BriefSubject, OnlineContentEntry.Homework>>)
	}

	val quiz = onlineContents.mapResource {
		@Suppress("UNCHECKED_CAST")
		it.filter { (_, entry) ->
			entry is OnlineContentEntry.Quiz
		} as List<Pair<BriefSubject, OnlineContentEntry.Quiz>>
	}

	val timetable = home.mapResource {
		it.timetable
	}

	val timetableVal = timetable.map {
		if(it is ViewResource.Success) it.value
		else null
	}.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), null)

	val todaySchedule = timetable.mapResource { timetable ->
		val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
		timetable.entries.filter { it.day == day - 1 }.sortedBy { it.time }
	}

	private fun isImpending(time: Int): Boolean {
		return 0 < time && time < TimeUnit.HOURS.toNanos(24)
	}
}
