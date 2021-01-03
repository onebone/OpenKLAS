package org.openklas.ui.home

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
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.HomeFragmentBinding
import org.openklas.ui.common.ActivityViewModel
import org.openklas.utils.dp2px
import org.openklas.widget.TitleView

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = createAndBindView(inflater, R.layout.home_fragment, container)

		val viewModel = getViewModel<HomeViewModel>()

		mBinding.listMain.apply {
			addItemDecoration(RecyclerMarginDecoration(dp2px(context, 10f).toInt()))

			adapter = HomeMainAdapter(viewModel, viewLifecycleOwner).apply {
				// TODO make it changeable
				submitList(listOf(HomeViewType.SCHEDULE, HomeViewType.HOMEWORK))
			}
		}

		return view
	}

	override fun onResume() {
		super.onResume()

		val activityViewModel by activityViewModels<ActivityViewModel>()
		activityViewModel.title.value = resources.getString(R.string.app_name)
		activityViewModel.titleHeaderType.value = TitleView.HeaderType.HAMBURGER
		activityViewModel.titleSearchType.value = TitleView.SearchType.NONE
	}
}
