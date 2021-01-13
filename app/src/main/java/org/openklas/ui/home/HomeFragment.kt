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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.HomeFragmentBinding
import org.openklas.ui.common.configureTitle
import org.openklas.ui.postlist.PostType
import org.openklas.utils.dp2px
import org.openklas.widget.TitleView

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeFragmentBinding>() {
	private lateinit var navController: NavController
	private lateinit var viewModel: HomeViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = createAndBindView(inflater, R.layout.home_fragment, container)

		viewModel = getViewModel()

		mBinding.listMain.apply {
			addItemDecoration(RecyclerMarginDecoration(dp2px(context, 10f).toInt()))

			adapter = HomeMainAdapter(viewModel, this@HomeFragment).apply {
				// TODO make the order it changeable
				submitList(listOf(HomeViewType.SCHEDULE, HomeViewType.HOMEWORK, HomeViewType.ONLINE_CONTENTS))
			}
		}

		val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController
		mBinding.navView.setNavigationItemSelectedListener(DrawerNavigationListener())

		return view
	}

	fun onClickShowMore(v: View) {
		findNavController().navigate(HomeFragmentDirections.actionHomeTimetable())
	}

	override fun onResume() {
		super.onResume()

		configureTitle(resources.getString(R.string.app_name),
			TitleView.HeaderType.HAMBURGER, TitleView.SearchType.NONE)
	}

	internal inner class DrawerNavigationListener: NavigationView.OnNavigationItemSelectedListener {
		override fun onNavigationItemSelected(item: MenuItem): Boolean {
			navController.navigate(when(item.itemId) {
				R.id.nav_notice_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.semester.value?.id, PostType.NOTICE)

				R.id.nav_material_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.semester.value?.id, PostType.LECTURE_MATERIAL)

				R.id.nav_qna_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.semester.value?.id, PostType.QNA)

				else ->
					throw IllegalStateException("invalid drawer menu selected: ${item.itemId}")
			})

			return true
		}
	}
}
