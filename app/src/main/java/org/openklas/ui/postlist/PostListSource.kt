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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.PostType
import org.openklas.repository.KlasRepository
import org.openklas.utils.Resource

class PostListSource(
	private val klasRepository: KlasRepository,
	private val sessionViewModelDelegate: SessionViewModelDelegate,
	private val query: PostListQuery?,
	private val errorHandler: (Throwable) -> Unit,
	private val pageInfoCallback: (Board.PageInfo) -> Unit,
): PagingSource<Int, Board.Entry>(), SessionViewModelDelegate by sessionViewModelDelegate {
	override fun getRefreshKey(state: PagingState<Int, Board.Entry>): Int? {
		return state.anchorPosition
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Board.Entry> {
		val key = (params.key ?: 0).coerceAtLeast(0)

		val query = query ?: return LoadResult.Page(
			data = listOf(), prevKey = null, nextKey = null
		)

		return when(val result = request(query, key)) {
			is Resource.Error -> {
				errorHandler(result.error)
				LoadResult.Error(result.error)
			}
			is Resource.Success -> {
				val page = result.value.pageInfo
				pageInfoCallback(page)

				LoadResult.Page(
					data = result.value.posts.toList(),
					prevKey = if(key <= 0) null else key - 1,
					nextKey = if(page.currentPage < page.totalPages - 1) key + 1 else null,
					itemsBefore = ((page.currentPage - 1) * page.postsPerPage).coerceIn(0, page.totalPosts),
					itemsAfter = (page.totalPosts - (page.currentPage + 1) * page.postsPerPage).coerceAtLeast(0)
				)
			}
		}
	}

	private suspend fun request(query: PostListQuery, page: Int): Resource<Board> {
		return requestWithSession {
			when(query.type) {
				PostType.NOTICE -> klasRepository.getNotices(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
				PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterials(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
				PostType.QNA -> klasRepository.getQnas(query.semester.id, query.subject.id, page, query.searchCriteria, query.keyword)
			}
		}
	}
}
