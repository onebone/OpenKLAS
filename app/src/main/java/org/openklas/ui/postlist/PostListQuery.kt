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

import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.klas.model.PostType

internal data class PostListQuery(
	val semester: Semester,
	val subject: BriefSubject,
	val type: PostType,
	val searchCriteria: BoardSearchCriteria,
	val keyword: String?
)
