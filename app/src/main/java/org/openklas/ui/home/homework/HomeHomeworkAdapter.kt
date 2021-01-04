package org.openklas.ui.home.homework

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
import org.openklas.databinding.HomeHomeworkItemBinding
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.OnlineContentEntry

class HomeHomeworkAdapter: ListAdapter<Pair<BriefSubject, OnlineContentEntry.Homework>, HomeHomeworkAdapter.HomeworkViewHolder>(SimpleDiffUtil()) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
		return HomeworkViewHolder(HomeHomeworkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
		val (subject, entry) = getItem(position)
		holder.bind(subject, entry)
	}

	class HomeworkViewHolder(private val binding: HomeHomeworkItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(subject: BriefSubject, homework: OnlineContentEntry.Homework) {
			binding.subject = subject
			binding.entry = homework
		}
	}
}
