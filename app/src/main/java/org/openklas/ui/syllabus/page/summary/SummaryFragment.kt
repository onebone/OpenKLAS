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

package org.openklas.ui.syllabus.page.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.openklas.databinding.SyllabusPageSummaryFragmentBinding
import org.openklas.ui.syllabus.SyllabusViewModel

class SummaryFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = SyllabusPageSummaryFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}

		val viewModel by viewModels<SyllabusViewModel>(ownerProducer = { requireParentFragment() })

		binding.viewModel = viewModel

		binding.rvTutors.adapter = TutorAdapter()
		binding.rvBooks.adapter = BookAdapter()

		viewModel.syllabus.observe(viewLifecycleOwner) {
			binding.entry = it
		}

		return binding.root
	}
}
