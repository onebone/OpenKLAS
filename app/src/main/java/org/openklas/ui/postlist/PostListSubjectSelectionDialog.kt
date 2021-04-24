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

package org.openklas.ui.postlist

import androidx.fragment.app.viewModels
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.ui.shared.SemesterSubjectDialog

class PostListSubjectSelectionDialog: SemesterSubjectDialog<PostListViewModel>() {
	override val viewModel by viewModels<PostListViewModel>(ownerProducer = { requireParentFragment() })

	override fun onDone(semester: Semester, subject: BriefSubject) {
		viewModel.setCurrentSemester(semester.id)
		viewModel.setSubject(subject.id)

		dismiss()
	}
}
