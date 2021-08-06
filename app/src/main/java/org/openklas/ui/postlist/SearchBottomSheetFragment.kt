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

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.openklas.R
import org.openklas.databinding.BottomPostListSearchBinding

internal class SearchBottomSheetFragment: BottomSheetDialogFragment() {
	private val _flow = MutableSharedFlow<SearchFragmentEvent>(
		extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
	)
	val flow: SharedFlow<SearchFragmentEvent> = _flow

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val themedInflater = inflater.cloneInContext(ContextThemeWrapper(
			requireContext(),
			R.style.Theme_OpenKLAS_BottomSheetContent
		))
		val binding = BottomPostListSearchBinding.inflate(themedInflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}

		binding.btnSearchSubmit.setOnClickListener {
			_flow.tryEmit(SearchEvent(binding.etKeyword.editText!!.text.toString()))
		}

		binding.etKeyword.editText!!.setOnEditorActionListener { v, actionId, _ ->
			return@setOnEditorActionListener when(actionId) {
				EditorInfo.IME_ACTION_SEND -> {
					_flow.tryEmit(SearchEvent(binding.etKeyword.editText!!.text.toString()))
					true
				}
				else -> false
			}
		}

		return binding.root
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)

		_flow.tryEmit(DismissEvent)
	}
}

sealed class SearchFragmentEvent

data class SearchEvent(val keyword: String): SearchFragmentEvent()
object DismissEvent: SearchFragmentEvent()
