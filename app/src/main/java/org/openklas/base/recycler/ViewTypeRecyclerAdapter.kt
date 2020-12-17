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

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.benoitquenaudon.rxdatabinding.databinding.RxObservableArrayMap
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus


abstract class ViewTypeRecyclerAdapter<Item> :
	RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>() {
	protected val mSelectedMap = ObservableArrayMap<Int, Boolean>()
	protected val mItemList: ArrayList<Item> = arrayListOf()
	protected lateinit var mContext: Context

	protected abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding
	override fun getItemCount() = mItemList.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
		mContext = parent.context
		val inflater = LayoutInflater.from(mContext)
		val binding = createBinding(inflater, parent, viewType)
		return BindingViewHolder(binding)
	}

	override fun onBindViewHolder(viewHolder: BindingViewHolder<ViewDataBinding>, position: Int) {
		val item = mItemList[position]
		bindViewType(viewHolder.binding, item, position)
		viewHolder.binding.executePendingBindings()
	}

	protected open fun bindViewType(binding: ViewDataBinding, item: Item, position: Int) {

	}

	protected fun getItemList() = mItemList

	/**
	 * send Event within EventBus
	 *
	 * @param event
	 */
	protected fun postEvent(event: Any) {
		EventBus.getDefault().post(event)
	}

	/**
	 * Update item with DiffUtils
	 */
	fun update(items: List<Item>?) {
		if (items == null) {
			return
		}

		updateAdapterWithDiffResult(calculateDiff(items))
	}

	/**
	 * Add item into adapter
	 */
	fun add(item: Item?) {
		item?.let {
			mItemList.add(it)
			notifyDataSetChanged()
		}
	}

	/**
	 * add all item of items into adapter
	 */
	fun addAll(items: List<Item>?) {
		if (items == null) {
			return
		}

		mItemList.addAll(items)
		notifyDataSetChanged()
	}

	/**
	 * set item into adapter
	 */
	fun setItems(items: List<Item>?) {
		clear()
		if (items != null) {
			addAll(items)
		}
	}

	/**
	 * Remove item from adapter
	 */
	fun remove(position: Int) {
		mItemList.removeAt(position)
		notifyDataSetChanged()
	}

	/**
	 * Clear item in adapter
	 */
	fun clear() {
		mItemList.clear()
		clearSelectedMap()
		notifyDataSetChanged()
	}

	/**
	 * get item with given position
	 */
	fun getItem(position: Int) = mItemList[position]

	/**
	 * true if itemList have given item
	 */
	fun contains(item: Item) = mItemList.contains(item)

	/**
	 * true if position has selected.
	 */
	fun isSelected(position: Int): Boolean = mSelectedMap[position] ?: false

	/**
	 * true if given item has selected
	 */
	fun isSelected(item: Item) = isSelected(mItemList.indexOf(item))

	/**
	 * set selected state of item which has given position
	 */
	fun setSelected(position: Int, isSelect: Boolean) {
		mSelectedMap[position] = isSelect
	}

	/**
	 * set selected state of item which has given item
	 */
	fun setSelected(item: Item, isSelect: Boolean) {
		setSelected(mItemList.indexOf(item), isSelect)
	}

	fun setAllSelected(isSelect: Boolean) {
		for (item in mItemList) {
			setSelected(item, isSelect)
		}
		notifyDataSetChanged()
	}

	/**
	 * get selected item list
	 */
	fun getSelectedItems(): List<Item> = mSelectedMap.entries
		.filter { it.value }
		.map { it.key }
		.map { mItemList[it] }
		.toList()

	/**
	 * clear selected item map
	 */
	fun clearSelectedMap() {
		mSelectedMap.clear()
	}

	fun getSelectedMapChanges(): Observable<ObservableArrayMap<Int, Boolean>> {
		return RxObservableArrayMap.mapChanges(mSelectedMap)
			.map { it.observableArrayMap() }
	}

	private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
		result.dispatchUpdatesTo(this)
	}

	private fun calculateDiff(newItems: List<Item>) = DiffUtil.calculateDiff(DiffUtilCallback(mItemList, newItems))

	internal class DiffUtilCallback<Item>(private val oldItems: List<Item>, private val newItems: List<Item>) : DiffUtil.Callback() {

		override fun getOldListSize() = oldItems.size

		override fun getNewListSize() = newItems.size

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition] == newItems[newItemPosition]

		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition] == newItems[newItemPosition]
	}
}
