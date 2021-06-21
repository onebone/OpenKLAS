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

package org.openklas.ui.post

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openklas.base.SessionViewModelDelegate
import org.openklas.base.SubjectViewModelDelegate
import org.openklas.klas.model.Attachment
import org.openklas.klas.model.PostComposite
import org.openklas.klas.model.PostType
import org.openklas.repository.KlasRepository
import org.openklas.utils.Resource
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
	private val klasRepository: KlasRepository,
	sessionViewModelDelegate: SessionViewModelDelegate,
	subjectViewModelDelegate: SubjectViewModelDelegate
): ViewModel(),
	SessionViewModelDelegate by sessionViewModelDelegate,
	SubjectViewModelDelegate by subjectViewModelDelegate {

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
					is Resource.Success -> postValue(result.value)
					is Resource.Error -> _error.postValue(result.error)
				}
			}
		}
	}

	private val _error = MutableLiveData<Throwable>()
	val error: LiveData<Throwable> = _error

	fun fetchPost(semester: String, subject: String, type: PostType, boardNo: Int, masterNo: Int) {
		viewModelScope.launch {
			val result = requestWithSession {
				when(type) {
					PostType.NOTICE -> klasRepository.getNotice(semester, subject, boardNo, masterNo)
					PostType.LECTURE_MATERIAL -> klasRepository.getLectureMaterial(semester, subject, boardNo, masterNo)
					PostType.QNA -> klasRepository.getQna(semester, subject, boardNo, masterNo)
				}
			}

			@SuppressLint("NullSafeMutableLiveData")
			when(result) {
				is Resource.Success -> postComposite.postValue(result.value)
				is Resource.Error -> _error.postValue(result.error)
			}
		}
	}
}
