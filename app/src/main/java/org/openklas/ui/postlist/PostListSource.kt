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

import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import org.openklas.klas.model.Board
import org.openklas.repository.KlasRepository

class PostListSource(
	private val klasRepository: KlasRepository,
	private val compositeDisposable: CompositeDisposable,
	private val query: Query?,
	private val pageInfoCallback: (Board.PageInfo) -> Unit
): PageKeyedDataSource<Int, Board.Entry>() {
	override fun loadInitial(
		params: LoadInitialParams<Int>,
		callback: LoadInitialCallback<Int, Board.Entry>
	) {
		// there is nothing to show when query is not set
		val query = query ?: return callback.onResult(listOf(), 0, 0, null, null)

		compositeDisposable.add(klasRepository.getNotices(query.semester, query.subject, 0).subscribe { v, e ->
			if(e != null) throw e

			val page = v.pageInfo
			pageInfoCallback(page)
			callback.onResult(v.posts.toList(), 0, page.totalPosts, null, if(page.currentPage < page.totalPages - 1) page.currentPage + 1 else null)
		})
	}

	override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Board.Entry>) {
		val query = query ?: return callback.onResult(listOf(), null)

		compositeDisposable.add(klasRepository.getNotices(query.semester, query.subject, params.key).subscribe { v, e ->
			if(e != null) throw e

			val page = v.pageInfo
			pageInfoCallback(page)
			callback.onResult(v.posts.toList(), if(page.currentPage <= 0 || page.totalPages <= 0) null else page.currentPage - 1)
		})
	}

	override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Board.Entry>) {
		val query = query ?: return callback.onResult(listOf(), null)

		compositeDisposable.add(klasRepository.getNotices(query.semester, query.subject, params.key).subscribe { v, e ->
			if(e != null) throw e

			val page = v.pageInfo
			pageInfoCallback(page)
			callback.onResult(v.posts.toList(), if(page.currentPage < page.totalPages - 1) page.currentPage + 1 else null)
		})
	}

	// query provided here is considered validated already
	data class Query(
		val semester: String,
		val subject: String,
		val type: PostType
	)
}
