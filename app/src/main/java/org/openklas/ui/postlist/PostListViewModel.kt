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

package org.openklas.ui.postlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.base.SubjectViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.PostType
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.repository.KlasRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PostListViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	subjectViewModelDelegate: SubjectViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SubjectViewModelDelegate by subjectViewModelDelegate {

	private val _filter = MutableStateFlow<Filter?>(value = null)
	val filter: StateFlow<Filter?> = _filter

	private val postType = MutableSharedFlow<PostType>(
		replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
	)

	private var pagingSource: PostListSource? = null

	private val query = combine(
		currentSemester.asFlow(),
		currentSubject.asFlow(),
		postType.distinctUntilChanged(),
		_filter.distinctUntilChangedBy { it?.criteria to it?.keyword }
	) { semester, subject, postType, filter ->
		PostListQuery(
			semester = semester,
			subject = subject,
			type = postType,
			searchCriteria = filter?.criteria ?: BoardSearchCriteria.ALL,
			keyword = filter?.keyword
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	val posts = query.flatMapLatest {
		Pager(
			config = PagingConfig(pageSize = 15),
			pagingSourceFactory = {
				pagingSource?.invalidate()

				val source = PostListSource(
					klasRepository = klasRepository,
					sessionViewModelDelegate = sessionViewModelDelegate,
					query = it,
					errorHandler = {
						_error.postValue(it)
					},
					pageInfoCallback = {
						pageInfo.tryEmit(it)
					}
				)

				pagingSource = source
				source
			}
		).flow
	}.cachedIn(viewModelScope)

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val pageInfo = MutableSharedFlow<Board.PageInfo?>(
		replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
	)
	// https://github.com/Kotlin/kotlinx.coroutines/issues/2631
	val postCount = pageInfo.map {
		it?.totalPosts
	}.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), null)

	fun setPostType(type: PostType) {
		postType.tryEmit(type)
	}

	fun setFilter(criteria: BoardSearchCriteria, keyword: String) {
		_filter.value = Filter(criteria, keyword)
	}

	fun clearFilter() {
		_filter.value = null
	}

	data class Filter(
		val criteria: BoardSearchCriteria,
		val keyword: String
	)
}
