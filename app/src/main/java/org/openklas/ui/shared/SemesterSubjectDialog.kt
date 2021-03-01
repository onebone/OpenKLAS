package org.openklas.ui.shared

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

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.databinding.DialogSemesterSubjectBinding
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester

abstract class SemesterSubjectDialog<T: SemesterViewModelDelegate>: DialogFragment() {
	protected abstract val viewModel: T
	private lateinit var binding: DialogSemesterSubjectBinding

	private val subjectAdapter = SubjectAdapter {
		finalize(it)
	}

	private val semesterAdapter = SemesterAdapter {
		subjectAdapter.submitList(it.subjects.toList())
		binding.optionSwitcher.showNext()
	}

	abstract fun onDone(semester: Semester, subject: BriefSubject)

	private fun finalize(subject: BriefSubject) {
		val semester = semesterAdapter.selectedSemester
			?: // This should not happen. Semester must be selected when subject is selected
			throw IllegalStateException("Please file a bug report with a procedures to reproduce the problem")

		onDone(semester, subject)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(requireContext()).create()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DialogSemesterSubjectBinding.inflate(layoutInflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}

		binding.viewModel = viewModel

		binding.rvSemesters.adapter = semesterAdapter
		binding.rvSubjects.adapter = subjectAdapter

		binding.tvNext.setOnClickListener {
			binding.optionSwitcher.showNext()
		}

		binding.tvPrevious.setOnClickListener {
			binding.optionSwitcher.showPrevious()
		}

		(requireDialog() as AlertDialog).setView(binding.root)

		return binding.root
	}
}
