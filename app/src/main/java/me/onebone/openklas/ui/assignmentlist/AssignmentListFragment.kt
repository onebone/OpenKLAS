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

package me.onebone.openklas.ui.assignmentlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.klas.model.AssignmentEntry
import me.onebone.openklas.ui.shared.compose.base.KlasTheme
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class AssignmentListFragment: BaseFragment() {
	private val args by navArgs<AssignmentListFragmentArgs>()
	private val viewModel by viewModels<AssignmentListViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		args.subject?.let {
			viewModel.setSubject(it)
		}

		args.semester?.let {
			viewModel.setCurrentSemester(it)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(
			resources.getString(R.string.assignment_list_title),
			AppbarView.HeaderType.BACK,
			AppbarView.SearchType.NONE
		)

		val onClickEntry = { assignment: AssignmentEntry ->
			val semester = viewModel.currentSemester.value?.id
			val subject = viewModel.currentSubject.value?.id

			if(semester != null && subject != null) {

				findNavController().navigate(
					AssignmentListFragmentDirections.actionAssignmentlistAssignment(
						semester = semester,
						subject = subject,
						order = assignment.order
					)
				)
			}
		}

		return ComposeView(requireContext()).apply {
			setContent {
				KlasTheme {
					AssignmentListScreen(
						onClickEntry = onClickEntry
					)
				}
			}
		}.apply {
			layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
		}
	}
}
