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
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.openklas.R
import org.openklas.base.recycler.BaseRecyclerAdapter
import org.openklas.databinding.ItemHomeTodayBinding
import org.openklas.klas.model.Timetable


class HomeTodayAdapter : BaseRecyclerAdapter<Timetable.Entry, ItemHomeTodayBinding>() {
	override fun bind(binding: ItemHomeTodayBinding, item: Timetable.Entry, position: Int) {
		binding.adapter = this
		binding.bean = item
		binding.position = position
	}

	override fun onClickedItem(binding: ItemHomeTodayBinding, item: Timetable.Entry, position: Int) {

	}

	override fun onLongClickedItem(binding: ItemHomeTodayBinding, item: Timetable.Entry, position: Int): Boolean {
		return false
	}

	override fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
		return DataBindingUtil.inflate(inflater, R.layout.item_home_today, parent, false)
	}
}