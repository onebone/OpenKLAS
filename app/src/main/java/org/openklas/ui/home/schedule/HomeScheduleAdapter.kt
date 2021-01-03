package org.openklas.ui.home.schedule

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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
import org.openklas.databinding.HomeScheduleItemBinding
import org.openklas.klas.model.Timetable

class HomeScheduleAdapter: ListAdapter<Timetable.Entry, HomeScheduleAdapter.ViewHolder>(SimpleDiffUtil { it.subjectId }) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(HomeScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		 holder.bind(getItem(position))
	}

	class ViewHolder(private val binding: HomeScheduleItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(entry: Timetable.Entry) {
			binding.entry = entry
		}
	}
}
