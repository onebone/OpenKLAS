package org.openklas.ui.postlist

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

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.PostType
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate {
	private var postType: PostType? = null
	private var filter: Filter? = null

	private var subjectSelector: (Array<BriefSubject>) -> BriefSubject? = {
		it.firstOrNull()
	}

	private val _subject = MutableLiveData<BriefSubject>()
	val subject: LiveData<BriefSubject> = MediatorLiveData<BriefSubject>().apply {
		addSource(subjects) {
			// do NOT use switchMap as [subject] always depends on [subjects] live data
			invokeSelector(it)
		}

		addSource(_subject) {
			value = it
		}
	}

	val posts = Transformations.switchMap(subject) {
		LivePagedListBuilder(object : DataSource.Factory<Int, Board.Entry>() {
			override fun create(): DataSource<Int, Board.Entry> =
				PostListSource(klasRepository, viewModelScope, buildQuery(), { _error.value = it }) {
					pageInfo.postValue(it)
				}
		}, PagedList.Config.Builder().setPageSize(15).build()).build()
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val pageInfo = MutableLiveData<Board.PageInfo>()
	val postCount: LiveData<Int> = Transformations.map(pageInfo) {
		it.totalPosts
	}

	private fun hasQuery(): Boolean {
		return postType != null && subject.value != null && currentSemester.value != null
	}

	fun setPostType(type: PostType) {
		if(postType != type) {
			postType = type

			posts.value?.dataSource?.invalidate()
		}
	}

	fun setFilter(criteria: BoardSearchCriteria, keyword: String) {
		filter = Filter(criteria, keyword)

		posts.value?.dataSource?.invalidate()
	}

	fun setSubject(subjectId: String) {
		subjectSelector = { subjects ->
			subjects.find { it.id == subjectId }
		}

		subjects.value?.let {
			invokeSelector(it)
		}
	}

	fun buildQuery(): PostListQuery? {
		return if(hasQuery())
			PostListQuery(
				currentSemester.value!!,
				subject.value!!,
				postType!!,
				filter?.criteria ?: BoardSearchCriteria.ALL,
				filter?.keyword
			)
		else null
	}

	private fun invokeSelector(subjects: Array<BriefSubject>) {
		val subject = subjectSelector(subjects)
		if(subject == null && subjects.isNotEmpty()) return

		@SuppressLint("NullSafeMutableLiveData")
		if(_subject.value != subject && subject != null) {
			_subject.value = subject

			posts.value?.dataSource?.invalidate()
		}
	}

	internal data class Filter(
		val criteria: BoardSearchCriteria,
		val keyword: String
	)
}
