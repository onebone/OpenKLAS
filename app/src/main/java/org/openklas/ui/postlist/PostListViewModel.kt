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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.BriefSubject
import org.openklas.repository.KlasRepository
import org.openklas.utils.helper.PostListQueryCallback

class PostListViewModel @ViewModelInject constructor(
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

	private val subjectObserver = Observer<BriefSubject> {
		// intentional empty block
	}
	val posts by lazy {
		// Workaround for the problem that posts are not fetched if [subject] is not being observed.
		// The observer registered here is unsubscribed in onCleared().
		// This problem occurs when [posts] is being observed but [subject] is not observed, because
		// AAC does not trigger transformation if one is not being observed.
		// This is actually not a violation of the recommendation of the official Android document, which
		// prohibits ViewModel to observe lifecycle-aware objects such as LiveData, because this
		// observation is not bound to any lifecycle. As long as we remove observer in onCleared(),
		// there will be no potential memory leaks.
		_subject.observeForever(subjectObserver)

		LivePagedListBuilder(object: DataSource.Factory<Int, Board.Entry>() {
			override fun create(): DataSource<Int, Board.Entry> =
				PostListSource(klasRepository, compositeDisposable, queryResolver) {
					pageInfo.value = it
				}
		}, PagedList.Config.Builder().setPageSize(15).build()).build()
	}

	private val _subject = MediatorLiveData<BriefSubject>().apply {
		addSource(currentSemester) {
			// providing semester to resolver will make subject to be resolved
			queryResolver.setSemester(it)
		}
	}
	val subject: LiveData<BriefSubject> = _subject

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

	override fun onCleared() {
		super.onCleared()

		queryResolver.removeListener(queryResolvedListener)
		_subject.removeObserver(subjectObserver)
	}
}
