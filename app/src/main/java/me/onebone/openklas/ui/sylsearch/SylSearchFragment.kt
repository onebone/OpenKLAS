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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.databinding.SyllabusSearchFragmentBinding
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class SylSearchFragment: BaseFragment() {
	private val viewModel by viewModels<SylSearchViewModel>()
	private lateinit var binding: SyllabusSearchFragmentBinding

	private val currentYear = ZonedDateTime.now().year

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val fragment = childFragmentManager.findFragmentByTag(SearchBottomSheetFragmentTag)
		if(fragment is SearchBottomSheetFragment) {
			fragment.registerSearchEventHandler()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.syllabus_search), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		binding = SyllabusSearchFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}

		prepareViewModel(viewModel)

		binding.viewModel = viewModel
		binding.v = this

		binding.rvSyllabus.apply {
			adapter = SylSearchAdapter { item ->
				if(!item.isReady) {
					AlertDialog.Builder(requireContext())
						.setMessage(R.string.syllabus_search_no_data)
						.setPositiveButton(android.R.string.ok, null)
						.create()
						.show()
					return@SylSearchAdapter
				}

				val id = "U${item.year}${item.term}${item.openGwamokNo}${item.departmentCode}${item.division}${item.targetGrade}"
				findNavController().navigate(SylSearchFragmentDirections.actionSylsearchSyllabus(id))
			}

			addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
		}

		setAppbarOnClickSearchListener { _, cancel ->
			if(cancel) {
				val bottomSheet = SearchBottomSheetFragment.create(currentYear)
				bottomSheet.registerSearchEventHandler()
				bottomSheet.show(childFragmentManager, SearchBottomSheetFragmentTag)
			}
		}

		return binding.root
	}

	private fun SearchBottomSheetFragment.registerSearchEventHandler() {
		lifecycleScope.launch {
			// FIXME bottom sheet should be dismissed when a lifecycle state of this fragment
			// turns into paused state, or event handler should be reattached on recreation
			// of this fragment.
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				val event = flow.first()
				if(event is SearchEvent) {
					viewModel.setFilter(event.year, event.term, event.keyword, event.professor)
				}

				setAppbarSearchState(true)
				dismiss()
			}
		}
	}

	companion object {
		private const val SearchBottomSheetFragmentTag = "SylSearchSearchBottomSheetFragmentTag"
	}
}
