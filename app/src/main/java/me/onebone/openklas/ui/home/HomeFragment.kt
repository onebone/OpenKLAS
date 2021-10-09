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

package me.onebone.openklas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.widget.AppbarView
import java.time.ZonedDateTime
import me.onebone.openklas.utils.ViewResource

@AndroidEntryPoint
class HomeFragment: BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.app_name),
			AppbarView.HeaderType.HAMBURGER, AppbarView.SearchType.NONE)

		val viewModel by viewModels<HomeViewModel>()

		return ComposeView(requireContext()).apply {
			setContent {
				MaterialTheme {
					Surface(
						color = colorResource(R.color.super_light_gray),
						modifier = Modifier.fillMaxSize()
					) {
						val currentSemester by viewModel.currentSemester.observeAsState()
						val scheduleToday by viewModel.todaySchedule.collectAsState(ViewResource.Loading())

						val assignments by viewModel.homeworks.collectAsState(ViewResource.Loading())
						val impendingAssignments by viewModel.impendingHomework.collectAsState(ViewResource.Loading())

						val videos by viewModel.videos.collectAsState(ViewResource.Loading())
						val impendingVideos by viewModel.impendingVideo.collectAsState(ViewResource.Loading())

						HomeScreen(
							currentSemester = currentSemester,
							schedule = scheduleToday,
							now = ZonedDateTime.now(),
							assignments = assignments,
							impendingAssignments = impendingAssignments,
							videos = videos,
							impendingVideos = impendingVideos
						)
					}
				}
			}
		}
	}

	fun onClickShowMore(v: View) {
		findNavController().navigate(HomeFragmentDirections.actionHomeTimetable())
	}
}
