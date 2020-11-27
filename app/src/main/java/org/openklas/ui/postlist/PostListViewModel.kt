package org.openklas.ui.postlist

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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
import com.github.windsekirun.daggerautoinject.InjectViewModel
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Board
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.repository.KlasRepository
import javax.inject.Inject

@InjectViewModel
class PostListViewModel @Inject constructor(
	app: MainApplication,
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate
): BaseViewModel(app), SessionViewModelDelegate by sessionViewModelDelegate {
	// semester must be set externally, others are optional
	val type = MutableLiveData<PostType>()
	val subject = MutableLiveData<String>()
	val semester = MutableLiveData<String>()
	val page = MutableLiveData(0)

	private val semesters = MutableLiveData<Array<Semester>>()

	private val _semesterObject = MediatorLiveData<Semester>().apply {
		fun combine() {
			val currentSemester = semester.value ?: return
			val allSemesters = semesters.value ?: return

			this.value = allSemesters.find {
				it.id == currentSemester
			}
		}

		addSource(semesters) {
			combine()
		}

		addSource(semester) {
			combine()
		}
	}
	val semesterObject: LiveData<Semester> = _semesterObject

	private val _subjectObject = MediatorLiveData<BriefSubject>().apply {
		fun combine() {
			val currentSubject = subject.value ?: return
			val currentSemester = semesterObject.value ?: return

			this.value = currentSemester.subjects.find {
				it.id == currentSubject
			}
		}

		addSource(semesterObject) {
			combine()
		}

		addSource(subject) {
			combine()
		}
	}
	val subjectObject: LiveData<BriefSubject> = _subjectObject

	private val _subjects = MediatorLiveData<Array<BriefSubject>>().apply {
		addSource(semesters) {
			setSubjects()
		}

		addSource(semester) {
			setSubjects()
		}
	}
	val subjects: LiveData<Array<BriefSubject>> = _subjects

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	private val board = MediatorLiveData<Board>().apply {
		addSource(semester) {
			fetchPosts()
		}

		addSource(subjects) {}

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

	val pageInfo: LiveData<Board.PageInfo> = Transformations.map(board) {
		it.pageInfo
	}

	init {
		fetchSemesters()
	}

	private fun fetchSemesters() {
		addDisposable(requestWithSession {
			klasRepository.semesters
		}.subscribe { v, err ->
			if(err == null) {
				semesters.value = v
			}else{
				_error.value = err
			}
		})
	}

	private fun setSubjects() {
		val currentSemester = semester.value ?: return

		_subjects.value = semesters.value?.first {
			it.id == currentSemester
		}?.subjects

		if(subject.value == null && _subjects.value?.isNotEmpty() == true) {
			subject.value = _subjects.value!![0].id
		}
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
