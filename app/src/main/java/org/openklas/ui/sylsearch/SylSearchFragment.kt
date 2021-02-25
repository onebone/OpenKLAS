package org.openklas.ui.sylsearch

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.SyllabusSearchFragmentBinding
import org.openklas.widget.AppbarView
import java.util.Calendar

@AndroidEntryPoint
class SylSearchFragment: BaseFragment() {
	private val viewModel by viewModels<SylSearchViewModel>()
	private lateinit var binding: SyllabusSearchFragmentBinding

	private val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.syllabus_search), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		binding = SyllabusSearchFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@SylSearchFragment
		}

		prepareViewModel(viewModel)

		binding.viewModel = viewModel
		binding.v = this

		binding.rvSyllabus.apply {
			adapter = SylSearchAdapter {
				findNavController().navigate(SylSearchFragmentDirections.actionSylsearchSyllabus(it))
			}

			addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
		}

		binding.spinnerYear.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Array(YEARS) {
			currentYear - it
		})

		binding.spinnerTerm.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.term_names, android.R.layout.simple_spinner_dropdown_item)

		val contentRoot = binding.contentRoot

		contentRoot.setOnCollapsedListener {
			setAppbarSearchState(false)
		}

		contentRoot.setOnExpandedListener {
			setAppbarSearchState(true)
		}

		setAppbarOnClickSearchListener { _, cancel ->
			if(cancel) {
				contentRoot.collapseBottomSheet()
			}else{
				contentRoot.expandBottomSheet()
			}
		}

		return binding.root
	}

	fun onClickQuerySubmit() {
		val year = currentYear - binding.spinnerYear.selectedItemPosition
		val term = binding.spinnerTerm.selectedItemPosition + 1
		val keyword = binding.etKeyword.text.toString()
		val professor = binding.etProfessor.text.toString()

		viewModel.setFilter(year, term, keyword, professor)

		binding.contentRoot.expandBottomSheet()
	}

	private companion object {
		const val YEARS = 11
	}
}
