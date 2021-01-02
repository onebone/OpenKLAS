package org.openklas.ui.home

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
