package org.openklas.ui.postlist

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.databinding.PostItemBinding
import org.openklas.klas.model.Board

class PostListAdapter: ListAdapter<Board.Entry, PostListAdapter.ViewHolder>(PostDiffUtil()) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context)))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val entry = getItem(position)
		holder.bind(entry)
	}

	class ViewHolder(val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(entry: Board.Entry) {
			binding.post = entry
			binding.executePendingBindings()
		}
	}

	class PostDiffUtil: DiffUtil.ItemCallback<Board.Entry>() {
		override fun areItemsTheSame(oldItem: Board.Entry, newItem: Board.Entry): Boolean {
			return oldItem.boardNo == newItem.boardNo
		}

		override fun areContentsTheSame(oldItem: Board.Entry, newItem: Board.Entry): Boolean {
			return oldItem == newItem
		}

	}
}
