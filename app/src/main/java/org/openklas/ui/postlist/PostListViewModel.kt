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
	// board type, target subject, target page, current semester is an argument to search posts
	private val targetType = MutableLiveData<PostType>()
	private val targetSubject = MutableLiveData<String>()
	private val targetPage = MutableLiveData(0)

	private val _subject = MediatorLiveData<BriefSubject>().apply {
		fun combine() {
			val currentSubject = targetSubject.value ?: return
			val currentSemester = currentSemester.value ?: return

			val newValue = currentSemester.subjects.find {
				it.id == currentSubject
			} ?: currentSemester.subjects.firstOrNull()

			if(value != newValue) value = newValue

			fetchPosts()
		}

		addSource(currentSemester) {
			combine()
		}

		addSource(targetSubject) {
			combine()
		}
	}
	val subject: LiveData<BriefSubject> = _subject

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val board = MutableLiveData<Board>()

	val posts: LiveData<Array<Board.Entry>> = Transformations.map(board) {
		it.posts
	}

	val postCount: LiveData<Int> = Transformations.map(posts) {
		it.size
	}

	val pageInfo: LiveData<Board.PageInfo> = Transformations.map(board) {
		it.pageInfo
	}

	fun hasQuery(): Boolean {
		return currentSemester.value != null && targetSubject.value != null
				&& targetType.value != null && targetPage.value!! >= 0
	}

	fun setQuery(semester: String, subject: String, type: PostType, page: Int) {
		setCurrentSemester(semester)
		targetSubject.value = subject
		targetType.value = type
		targetPage.value = page
	}

	// called from res/layout/post_list_fragment.xml
	fun onSubjectIndexChanged(index: Int) {
		val allSubjects = subjects.value ?: return
		if(allSubjects.lastIndex < index) return

		targetSubject.value = allSubjects[index].id
	}

	private fun fetchPosts() {
		val queryType = targetType.value
		val querySubject = subject.value
		val querySemester = currentSemester.value?.id
		val queryPage = targetPage.value!!

		if(queryType == null || querySubject == null || querySemester == null) return
		if(queryPage < 0) return

		addDisposable(requestWithSession {
			when(queryType) {
				PostType.NOTICE -> klasRepository.getNotices(querySemester, querySubject.id, queryPage)
				PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterials(querySemester, querySubject.id, queryPage)
				PostType.QNA -> klasRepository.getQnas(querySemester, querySubject.id, queryPage)
			}
		}.subscribe { v, err ->
			if(err == null) {
				board.value = v
			}else{
				_error.value = err
			}
		})
	}
}
