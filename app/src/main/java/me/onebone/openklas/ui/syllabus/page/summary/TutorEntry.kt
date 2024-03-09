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

package me.onebone.openklas.ui.syllabus.page.summary

import me.onebone.openklas.R

val TUTOR_PROFESSOR = R.string.professor
val TUTOR_SECONDARY_PROFESSOR = R.string.secondary_professor
val TUTOR_TEACHING_ASSISTANT = R.string.teaching_assistant

data class TutorEntry(
	val name: String,
	val type: Int, // one of: R.string.professor, R.string.secondary_professor, R.string.teaching_assistant
	val email: String?,
	val contact: String?,
	val telephoneContact: String?
)
