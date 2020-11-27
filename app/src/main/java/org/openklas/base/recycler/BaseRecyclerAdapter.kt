package org.openklas.base.recycler

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

import androidx.databinding.ViewDataBinding


abstract class BaseRecyclerAdapter<Item, V : ViewDataBinding> : ViewTypeRecyclerAdapter<Item>() {

	protected abstract fun onClickedItem(binding: V, item: Item, position: Int)
	protected abstract fun onLongClickedItem(binding: V, item: Item, position: Int): Boolean
	protected abstract fun bind(binding: V, item: Item, position: Int)

	override fun bindViewType(binder: ViewDataBinding, item: Item, position: Int) {
		super.bindViewType(binder, item, position)
		val binding = binder as V

		bind(binding, item, position)

		binding.root.setOnClickListener { onClickedItem(binding, item, position) }
		binding.root.setOnLongClickListener { onLongClickedItem(binding, item, position) }
	}
}
