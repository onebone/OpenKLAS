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
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.TimetableFragmentBinding
import org.openklas.ui.common.configureTitle
import org.openklas.widget.TitleView

@AndroidEntryPoint
class TimetableFragment: BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = TimetableFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@TimetableFragment
		}

		binding.viewModel = getViewModel()

		return binding.root
	}

	override fun onResume() {
		super.onResume()

		configureTitle(resources.getString(R.string.timetable), TitleView.HeaderType.BACK, TitleView.SearchType.NONE)
	}
}
