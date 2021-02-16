package org.openklas.ui.syllabus.page

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

import androidx.annotation.IntDef
import org.openklas.R

const val TUTOR_PROFESSOR = R.string.professor
const val TUTOR_SECONDARY_PROFESSOR = R.string.secondary_professor
const val TUTOR_TEACHING_ASSISTANT = R.string.teaching_assistant

@Retention(AnnotationRetention.SOURCE)
@IntDef(TUTOR_PROFESSOR, TUTOR_SECONDARY_PROFESSOR, TUTOR_TEACHING_ASSISTANT)
annotation class TutorType

data class TutorEntry(
	val name: String,
	@TutorType val type: Int,
	val email: String?,
	val contact: String?,
	val telephoneContact: String?
)
