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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.HomeFragmentBinding
import org.openklas.utils.dp2px
import org.openklas.widget.AppbarView

@AndroidEntryPoint
class HomeFragment: BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.app_name),
			AppbarView.HeaderType.HAMBURGER, AppbarView.SearchType.NONE)

		val binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@HomeFragment
		}

		val viewModel = getViewModel<HomeViewModel>()

		binding.listMain.apply {
			addItemDecoration(RecyclerMarginDecoration(dp2px(context, 10f).toInt()))

			adapter = HomeMainAdapter(viewModel, this@HomeFragment).apply {
				// TODO make the order it changeable
				submitList(listOf(HomeViewType.SCHEDULE, HomeViewType.HOMEWORK, HomeViewType.ONLINE_CONTENTS))
			}
		}

		return binding.root
	}

	fun onClickShowMore(v: View) {
		findNavController().navigate(HomeFragmentDirections.actionHomeTimetable())
	}
}
