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

package org.openklas.ui.syllabus.page.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.openklas.R
import org.openklas.databinding.SyllabusPageScheduleFragmentBinding
import org.openklas.ui.syllabus.SyllabusViewModel

class ScheduleFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = SyllabusPageScheduleFragmentBinding.inflate(inflater, container, false)

		val viewModel by viewModels<SyllabusViewModel>(ownerProducer = { requireParentFragment() })
		binding.viewModel = viewModel

		viewModel.syllabus.observe(viewLifecycleOwner) {
			binding.entry = it
		}

		binding.rvWeeks.apply {
			addItemDecoration(WeekItemDecoration(ResourcesCompat.getColor(context.resources, R.color.green, null)))

			adapter = WeekAdapter()
		}

		return binding.root
	}
}
