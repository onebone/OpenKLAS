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

class PostListViewModel @ViewModelInject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(), SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate {
	private var query: Query? = null

	val posts = LivePagedListBuilder(object: DataSource.Factory<Int, Board.Entry>() {
		override fun create(): DataSource<Int, Board.Entry>
			= PostListSource(klasRepository, compositeDisposable,
				if(currentSemester.value == null || subject.value == null || !hasQuery()) null
				else PostListSource.Query(
					currentSemester.value!!.id, subject.value!!.id, query!!.type
				)
			) {
				pageInfo.value = it
			}
	}, PagedList.Config.Builder().setPageSize(15).build()).build()

	private val _subject = MediatorLiveData<BriefSubject>().apply {
		addSource(currentSemester) {
			if(hasQuery()) {
				val query = query!!

				resolveCurrentSubject(query.subject)?.let { subject ->
					this.value = subject

					posts.value?.dataSource?.invalidate()
				}
			}
		}
	}
	val subject: LiveData<BriefSubject> = _subject

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val pageInfo = MutableLiveData<Board.PageInfo>()
	val postCount: LiveData<Int> = Transformations.map(pageInfo) {
		it.totalPosts
	}

	fun hasQuery(): Boolean {
		return query?.isValid() == true
	}

	private fun resolveCurrentSubject(id: String): BriefSubject? {
		val currentSemester = currentSemester.value ?: return null

		return currentSemester.subjects.find { subject -> subject.id == id } ?: currentSemester.subjects.firstOrNull()
	}

	fun setQuery(semester: String, subject: String, type: PostType, page: Int) {
		setCurrentSemester(semester)

		val newQuery = Query(
			type = type, subject = subject, page = page
		)

		if(!newQuery.isValid())
			throw IllegalArgumentException("invalid query is given")

		query = newQuery

		if(currentSemester.value != null) {
			resolveCurrentSubject(subject)?.let {
				_subject.value = it

				posts.value?.dataSource?.invalidate()
			}
		}
	}

	private data class Query(
		val type: PostType,
		val subject: String,
		val page: Int
	) {
		fun isValid(): Boolean {
			return page >= 0
		}
	}
}
