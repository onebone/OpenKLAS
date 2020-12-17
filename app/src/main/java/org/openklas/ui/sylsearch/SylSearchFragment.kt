package org.openklas.ui.sylsearch

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.SyllabusSearchFragmentBinding

@AndroidEntryPoint
class SylSearchFragment: BaseFragment<SyllabusSearchFragmentBinding>() {
	private lateinit var viewModel: SylSearchViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = createAndBindView(inflater, R.layout.syllabus_search_fragment, container)

		viewModel = getViewModel()

		mBinding.viewModel = viewModel
		mBinding.v = this
		mBinding.list.layoutManager = LinearLayoutManager(mBinding.list.context)
		mBinding.list.adapter = SylSearchAdapter()
		return view
	}

	fun onClickBack(view: View) {
		this.requireActivity().onBackPressed()
	}

	fun onClickSearch(view: View, keyword: String) {
		viewModel.keyword.value = keyword
	}
}
