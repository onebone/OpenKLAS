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

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.openklas.klas.model.Board
import org.openklas.klas.model.PostType
import org.openklas.repository.KlasRepository
import org.openklas.utils.Result

class PostListSource(
	private val klasRepository: KlasRepository,
	private val coroutineScope: CoroutineScope,
	private val isInitialLoading: MutableLiveData<Boolean>,
	private val query: PostListQuery?,
	private val errorHandler: (Throwable) -> Unit,
	private val pageInfoCallback: (Board.PageInfo) -> Unit,
): PageKeyedDataSource<Int, Board.Entry>() {
	override fun loadInitial(
		params: LoadInitialParams<Int>,
		callback: LoadInitialCallback<Int, Board.Entry>
	) {
		// there is nothing to show when query is not set
		val query = query ?: return callback.onResult(listOf(), 0, 0, null, null)

		isInitialLoading.postValue(true)

		coroutineScope.launch {
			val result = request(query, 0)
			if(result is Result.Error) {
				errorHandler(result.error)
			}else if(result is Result.Success) {
				isInitialLoading.postValue(false)

				val board = result.value
				val page = board.pageInfo

				pageInfoCallback(page)
				callback.onResult(
					board.posts.toList(), 0, page.totalPosts, null,
					if(page.currentPage < page.totalPages - 1) page.currentPage + 1 else null
				)
			}
		}
	}

	override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Board.Entry>) {
		val query = query ?: return callback.onResult(listOf(), null)

		coroutineScope.launch {
			val result = request(query, params.key)

			if(result is Result.Error) {
				errorHandler(result.error)
			}else if(result is Result.Success) {
				val board = result.value
				val page = board.pageInfo

				pageInfoCallback(page)
				callback.onResult(board.posts.toList(), if(page.currentPage <= 0 || page.totalPages <= 0) null else page.currentPage - 1)
			}
		}
	}

	override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Board.Entry>) {
		val query = query ?: return callback.onResult(listOf(), null)

		coroutineScope.launch {
			val result = request(query, params.key)

			if(result is Result.Error) {
				errorHandler(result.error)
			}else if(result is Result.Success) {
				val board = result.value
				val page = board.pageInfo

				pageInfoCallback(page)
				callback.onResult(board.posts.toList(), if(page.currentPage < page.totalPages - 1) page.currentPage + 1 else null)
			}
		}
	}

	private suspend fun request(query: PostListQuery, page: Int): Result<Board> {
		return when(query.type) {
			PostType.NOTICE -> klasRepository.getNotices(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
			PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterials(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
			PostType.QNA -> klasRepository.getQnas(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
		}
	}
}
