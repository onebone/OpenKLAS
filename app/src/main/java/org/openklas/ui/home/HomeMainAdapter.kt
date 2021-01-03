package org.openklas.ui.home

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
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.base.list.SimpleDiffUtil
import org.openklas.databinding.HomeScheduleRootItemBinding
import org.openklas.ui.home.schedule.HomeScheduleAdapter

class HomeMainAdapter(
	private val viewModel: HomeViewModel,
	private val lifecycleOwner: LifecycleOwner
): ListAdapter<Int, HomeMainAdapter.HomeViewHolder>(SimpleDiffUtil()) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
		0 -> ScheduleViewHolder(HomeScheduleRootItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
		else -> throw IllegalStateException("invalid viewType given: $viewType")
	}

	override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
		holder.bind(viewModel)
	}

	override fun getItemViewType(position: Int): Int = getItem(position)

	inner class ScheduleViewHolder(private val binding: HomeScheduleRootItemBinding): HomeViewHolder(binding.root) {
		override fun bind(viewModel: HomeViewModel) {
			binding.viewModel = viewModel
			binding.lifecycleOwner = lifecycleOwner

			binding.rvSchedule.adapter = binding.rvSchedule.adapter ?: HomeScheduleAdapter()
		}
	}

	abstract class HomeViewHolder(root: View): RecyclerView.ViewHolder(root) {
		abstract fun bind(viewModel: HomeViewModel)
	}
}
