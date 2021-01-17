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
	// semester must be set externally, others are optional
	val type = MutableLiveData<PostType>()
	val subject = MutableLiveData<String>()
	val semester = MutableLiveData<String>()
	val page = MutableLiveData(0)

	private val _subjectObject = MediatorLiveData<BriefSubject>().apply {
		fun combine() {
			val currentSubject = subject.value ?: return
			val currentSemester = currentSemester.value ?: return

			this.value = currentSemester.subjects.find {
				it.id == currentSubject
			}
		}

		addSource(currentSemester) {
			combine()
		}

		addSource(subject) {
			combine()
		}
	}
	val subjectObject: LiveData<BriefSubject> = _subjectObject

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val board = MediatorLiveData<Board>().apply {
		addSource(semester) {
			fetchPosts()
		}

		addSource(subject) {
			fetchPosts()
		}

		addSource(type) {
			fetchPosts()
		}

		addSource(page) {
			fetchPosts()
		}
	}

	val posts: LiveData<Array<Board.Entry>> = Transformations.map(board) {
		it.posts
	}

	val postsIsEmpty: LiveData<Boolean> = Transformations.map(posts) {
		it.isEmpty()
	}

	val pageInfo: LiveData<Board.PageInfo> = Transformations.map(board) {
		it.pageInfo
	}

	// called from res/layout/post_list_fragment.xml
	fun onSubjectIndexChanged(index: Int) {
		val allSubjects = subjects.value ?: return
		if(allSubjects.lastIndex < index) return

		subject.value = allSubjects[index].id
	}

	private fun fetchPosts() {
		val queryType = type.value
		val querySubject = subject.value
		val querySemester = semester.value
		val queryPage = page.value!!

		if(queryType == null || querySubject == null || querySemester == null) return
		if(queryPage < 0) return

		addDisposable(requestWithSession {
			when(queryType) {
				PostType.NOTICE -> klasRepository.getNotices(querySemester, querySubject, queryPage)
				PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterials(querySemester, querySubject, queryPage)
				PostType.QNA -> klasRepository.getQnas(querySemester, querySubject, queryPage)
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
