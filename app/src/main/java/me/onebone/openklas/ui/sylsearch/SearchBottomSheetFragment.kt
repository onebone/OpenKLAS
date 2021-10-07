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

package me.onebone.openklas.ui.sylsearch

import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.SharedFlow
import me.onebone.openklas.R
import me.onebone.openklas.databinding.BottomSyllabusSearchBinding
import me.onebone.openklas.utils.SharedEventFlow
import me.onebone.openklas.utils.getSelection
import me.onebone.openklas.utils.getSelectionText

internal class SearchBottomSheetFragment: BottomSheetDialogFragment() {
	private val currentYear by lazy {
		requireArguments().getInt(KEY_CURRENT_YEAR)
	}

	private val _flow = SharedEventFlow<SearchDialogEvent>()
	val flow: SharedFlow<SearchDialogEvent> = _flow

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val themedInflater = inflater.cloneInContext(ContextThemeWrapper(
			requireContext(), R.style.Theme_OpenKLAS_BottomSheetContent
		))
		val binding = BottomSyllabusSearchBinding.inflate(themedInflater, container, false)

		(binding.spinnerYear.editText as AutoCompleteTextView).apply {
			setAdapter(ArrayAdapter(
				requireContext(),
				android.R.layout.simple_spinner_dropdown_item,
				Array(YEARS) { currentYear - it }
			))

			setText(currentYear.toString(), false)
		}

		val terms = resources.getStringArray(R.array.term_names)
		val termAdapter = ArrayAdapter(
			requireContext(),
			android.R.layout.simple_spinner_dropdown_item,
			terms
		)
		(binding.spinnerTerm.editText as AutoCompleteTextView).apply {
			setAdapter(termAdapter)

			setText(terms.first(), false)
		}

		binding.btnSubmit.setOnClickListener {
			_flow.tryEmit(SearchEvent(
				year = binding.spinnerYear.getSelectionText().toInt(),
				term = binding.spinnerTerm.getSelection(terms) + 1,
				keyword = binding.etKeyword.getSelectionText(),
				professor = binding.etProfessor.getSelectionText()
			))
		}

		return binding.root
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)

		_flow.tryEmit(DismissEvent)
	}

	companion object {
		private const val KEY_CURRENT_YEAR = "YEAR"

		private const val YEARS = 11

		fun create(currentYear: Int) =
			SearchBottomSheetFragment().apply {
				arguments = Bundle().also {
					it.putInt(KEY_CURRENT_YEAR, currentYear)
				}
			}
	}
}

internal sealed class SearchDialogEvent

internal object DismissEvent: SearchDialogEvent()
internal data class SearchEvent(
	val year: Int,
	val term: Int,
	val keyword: String,
	val professor: String
): SearchDialogEvent()
