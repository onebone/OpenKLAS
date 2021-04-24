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

package org.openklas.base.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class SimpleDiffUtil<T>(
	private val idCriteria: (T) -> Any? = { it },
	private val contentCriteria: (T) -> Any? = { it }
): DiffUtil.ItemCallback<T>() {
	override fun areItemsTheSame(oldItem: T, newItem: T) =
		idCriteria(oldItem) == idCriteria(newItem)

	@SuppressLint("DiffUtilEquals")
	override fun areContentsTheSame(oldItem: T, newItem: T) =
		contentCriteria(oldItem) == contentCriteria(newItem)
}
