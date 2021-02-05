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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.repository.KlasRepository
import org.openklas.utils.helper.PostListQueryCallback
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate {

	private val queryResolvedListener = PostListQueryCallback {
		// subject object is resolved as soon as query is resolved
		_subject.value = it.subject
	}
	private val queryResolver = PostListQueryResolver()

	private val _subject = MediatorLiveData<BriefSubject>().apply {
		addSource(currentSemester) {
			// providing semester to resolver will make subject to be resolved
			queryResolver.setSemester(it)
		}
	}
	val subject: LiveData<BriefSubject> = _subject

	val posts = Transformations.switchMap(subject) {
		LivePagedListBuilder(object: DataSource.Factory<Int, Board.Entry>() {
			override fun create(): DataSource<Int, Board.Entry> =
				PostListSource(klasRepository, compositeDisposable, queryResolver) {
					pageInfo.value = it
				}
		}, PagedList.Config.Builder().setPageSize(15).build()).build()
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val pageInfo = MutableLiveData<Board.PageInfo>()
	val postCount: LiveData<Int> = Transformations.map(pageInfo) {
		it.totalPosts
	}

	init {
		queryResolver.addListener(queryResolvedListener)
	}

	fun hasQuery(): Boolean {
		return queryResolver.resolvedQuery != null
	}

	fun setQuery(semester: String, subject: String, type: PostType) {
		setCurrentSemester(semester)

		with(queryResolver) {
			setSubject(subject)
			setType(type)
		}
	}

	fun setFilter(criteria: BoardSearchCriteria, keyword: String) {
		queryResolver.setFilter(criteria, keyword)
	}

	override fun onCleared() {
		super.onCleared()

		queryResolver.removeListener(queryResolvedListener)
	}
}
