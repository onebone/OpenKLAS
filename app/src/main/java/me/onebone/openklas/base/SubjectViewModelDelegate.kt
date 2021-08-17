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

package me.onebone.openklas.base

import androidx.lifecycle.LiveData
import me.onebone.openklas.klas.model.BriefSubject

interface SubjectViewModelDelegate: SemesterViewModelDelegate {
	val currentSubject: LiveData<BriefSubject>

	/**
	 * Sets subject id for the [currentSubject] to match with.
	 *
	 * **NOTE** that if you want to change semester and subject simultaneously, you
	 * should call [setSubject] first and then call [setCurrentSemester] to
	 * avoid unnecessary network API call.
	 *
	 * <code>
	 *     // You should call setSubject() first before setCurrentSemester() is called
	 *     viewModel.setSubject(args.subject)
	 *     viewModel.setCurrentSemester(args.semester)
	 * </code>
	 *
	 * Calling [setCurrentSemester] first will lead [SubjectViewModelDelegate] to change
	 * [currentSubject] matching current subject selector, which generally defaults
	 * to the first entry of the subjects array: resulting in [currentSubject] to be changed.
	 * Right after that, [setSubject] is called which changes subject selector
	 * and [currentSubject] will be changed twice.
	 *
	 * @param subjectId
	 */

	fun setSubject(subjectId: String)
}
