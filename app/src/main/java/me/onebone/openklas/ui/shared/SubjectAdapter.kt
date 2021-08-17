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

package me.onebone.openklas.ui.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.onebone.openklas.base.list.SimpleDiffUtil
import me.onebone.openklas.databinding.SemesterSubjectSelectionBinding
import me.onebone.openklas.klas.model.BriefSubject

class SubjectAdapter(
	private val onSubjectSelectedListener: (BriefSubject) -> Unit
): ListAdapter<BriefSubject, SubjectAdapter.ViewHolder>(SimpleDiffUtil { it.id }) {
	var selectedSubject: BriefSubject? = null
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
				onSubjectSelectedListener(value)
			}
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(SemesterSubjectSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class ViewHolder(val binding: SemesterSubjectSelectionBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(subject: BriefSubject) {
			binding.label = subject.name
			binding.selected = subject == selectedSubject

			binding.root.setOnClickListener {
				selectedSubject = subject
			}
		}
	}
}
