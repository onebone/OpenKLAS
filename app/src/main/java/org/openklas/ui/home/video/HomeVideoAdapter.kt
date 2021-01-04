package org.openklas.ui.home.video

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
import org.openklas.databinding.HomeOnlineVideoItemBinding
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.OnlineContentEntry

class HomeVideoAdapter: ListAdapter<Pair<BriefSubject, OnlineContentEntry.Video>, HomeVideoAdapter.ViewHolder>(SimpleDiffUtil { it.second.uri }) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(HomeOnlineVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val (subject, entry) = getItem(position)
		 holder.bind(subject, entry)
	}

	class ViewHolder(private val binding: HomeOnlineVideoItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(subject: BriefSubject, entry: OnlineContentEntry.Video) {
			binding.subject = subject
			binding.entry = entry
		}
	}
}
