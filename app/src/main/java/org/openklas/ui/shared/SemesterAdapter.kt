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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.base.list.SimpleDiffUtil
import org.openklas.databinding.SemesterSubjectSelectionBinding
import org.openklas.klas.model.Semester

class SemesterAdapter(
	private val onChangeSelectedSemesterListener: (Semester) -> Unit
): ListAdapter<Semester, SemesterAdapter.ViewHolder>(SimpleDiffUtil { it.id }) {
	var selectedSemester: Semester? = null
		set(value) {
			val oldItem = field

			field = value

			val currentList = currentList
			if(oldItem != null) {
				val oldItemIndex = currentList.indexOf(oldItem)
				if(oldItemIndex != -1)
					notifyItemChanged(oldItemIndex)
			}

			val newItemIndex = currentList.indexOf(value)
			if(newItemIndex != -1)
				notifyItemChanged(newItemIndex)

			if(value != null) {
				onChangeSelectedSemesterListener(value)
			}
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(SemesterSubjectSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class ViewHolder(val binding: SemesterSubjectSelectionBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(semester: Semester) {
			binding.label = semester.label
			binding.selected = semester == selectedSemester

			binding.root.setOnClickListener {
				selectedSemester = semester
			}
		}
	}
}
