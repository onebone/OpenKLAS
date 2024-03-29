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

package me.onebone.openklas.ui.sylsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.onebone.openklas.base.list.SimpleDiffUtil
import me.onebone.openklas.databinding.ItemSyllabusSearchBinding
import me.onebone.openklas.klas.model.SyllabusSummary

class SylSearchAdapter(
	private val onClickSyllabus: (SyllabusSummary) -> Unit
): ListAdapter<SyllabusSummary, SylSearchAdapter.ViewHolder>(SimpleDiffUtil()) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ItemSyllabusSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class ViewHolder(private val binding: ItemSyllabusSearchBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(item: SyllabusSummary) {
			binding.entry = item
			binding.root.setOnClickListener {
				onClickSyllabus(item)
			}
		}
	}
}
