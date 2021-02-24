package org.openklas.ui.post

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

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.klas.model.Attachment
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.PostComposite
import org.openklas.klas.model.PostType
import org.openklas.repository.KlasRepository
import org.openklas.utils.Result
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(),
	SessionViewModelDelegate by sessionViewModelDelegate,
	SemesterViewModelDelegate by semesterViewModelDelegate {

	private companion object {
		const val STORAGE_ID = "CLS_BOARD"
	}

	private val postComposite = MutableLiveData<PostComposite>()

	val post = Transformations.map(postComposite) {
		it.post
	}

	val attachments = Transformations.switchMap(post) {
		MutableLiveData<Array<Attachment>>().apply {
			if(it.attachmentId == null) {
				value = arrayOf()
				return@apply
			}

			viewModelScope.launch {
				val result = requestWithSession {
					klasRepository.getAttachments(STORAGE_ID, it.attachmentId)
				}

				when(result) {
					is Result.Success -> postValue(result.value)
					is Result.Error -> _error.postValue(result.error)
				}
			}
		}
	}

	private val currentSubjectId = MutableLiveData<String>()

	val subject: LiveData<BriefSubject> = MediatorLiveData<BriefSubject>().apply {
		fun combine() {
			val id = currentSubjectId.value ?: return
			val subjects = subjects.value ?: return

			subjects.find { it.id == id }?.let {
				value = it
			}
		}

		addSource(currentSubjectId) {
			combine()
		}

		addSource(subjects) {
			combine()
		}
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	fun fetchPost(type: PostType, boardNo: Int, masterNo: Int) {
		viewModelScope.launch {
			val result = requestWithSession {
				when(type) {
					PostType.NOTICE -> klasRepository.getNotice(boardNo, masterNo)
					PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterial(boardNo, masterNo)
					PostType.QNA -> klasRepository.getQna(boardNo, masterNo)
				}
			}

			@SuppressLint("NullSafeMutableLiveData")
			when(result) {
				is Result.Success -> postComposite.postValue(result.value)
				is Result.Error -> _error.postValue(result.error)
			}
		}
	}

	fun setSubjectId(id: String) {
		currentSubjectId.value = id
	}
}
