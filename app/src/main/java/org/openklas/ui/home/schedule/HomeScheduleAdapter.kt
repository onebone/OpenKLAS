package org.openklas.ui.home.schedule

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
