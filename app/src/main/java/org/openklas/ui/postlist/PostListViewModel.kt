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
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.base.SubjectViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.PostType
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	subjectViewModelDelegate: SubjectViewModelDelegate
): ViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SubjectViewModelDelegate by subjectViewModelDelegate {
	private var postType: PostType? = null
	private var filter: Filter? = null

	private val _isInitialLoading = MutableLiveData(true)
	val isInitialLoading: LiveData<Boolean> = _isInitialLoading

	private var pagingSource: PostListSource? = null

	val posts = Transformations.switchMap(currentSubject) {
		Pager(
			config = PagingConfig(
				pageSize = 15
			)
		) {
			pagingSource = PostListSource(
				klasRepository = klasRepository,
				sessionViewModelDelegate = sessionViewModelDelegate,
				query = buildQuery(),
				errorHandler = { _error.value = it },
				pageInfoCallback = { pageInfo.postValue(it) }
			)

			pagingSource!!
		}.liveData
	}.cachedIn(viewModelScope)

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val pageInfo = MutableLiveData<Board.PageInfo>()
	val postCount: LiveData<Int> = Transformations.map(pageInfo) {
		it.totalPosts
	}

	private fun hasQuery(): Boolean {
		return postType != null && currentSubject.value != null && currentSemester.value != null
	}

	fun setPostType(type: PostType) {
		if(postType != type) {
			postType = type

			pagingSource?.invalidate()
		}
	}

	fun setFilter(criteria: BoardSearchCriteria, keyword: String) {
		filter = Filter(criteria, keyword)

		pagingSource?.invalidate()
	}

	private fun buildQuery(): PostListQuery? {
		return if(hasQuery())
			PostListQuery(
				currentSemester.value!!,
				currentSubject.value!!,
				postType!!,
				filter?.criteria ?: BoardSearchCriteria.ALL,
				filter?.keyword
			)
		else null
	}

	internal data class Filter(
		val criteria: BoardSearchCriteria,
		val keyword: String
	)
}
