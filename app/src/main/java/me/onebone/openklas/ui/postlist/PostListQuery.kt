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

package me.onebone.openklas.ui.postlist

import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.klas.request.BoardSearchCriteria
import me.onebone.openklas.klas.model.PostType

internal data class PostListQuery(
	val semester: Semester,
	val subject: BriefSubject,
	val type: PostType,
	val searchCriteria: BoardSearchCriteria,
	val keyword: String?
)
