package org.openklas.ui.postlist

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

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import org.openklas.klas.model.BriefSubject

@BindingAdapter("subjects")
fun setSpinnerSubjectEntries(spinner: Spinner, subjects: Array<BriefSubject>?) {
	spinner.adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_dropdown_item, subjects?.map {
		it.name
	}?.toTypedArray() ?: arrayOf())
}

@BindingAdapter("onSubjectIndexChanged")
fun setSpinnerOnSubjectIndexChanged(spinner: Spinner, listener: ItemChangeListener) {
	spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
			listener.onIndexChanged(position)
		}

		override fun onNothingSelected(parent: AdapterView<*>?) {
		}
	}
}

@BindingAdapter("selectedIndex")
fun setSpinnerSelectedIndex(spinner: Spinner, index: Int) {
	spinner.setSelection(index)
}

@BindingAdapter("selectedIndexValueChanged")
fun setSpinnerInverseBindingListener(spinner: Spinner, listener: InverseBindingListener) {
	spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
			listener.onChange()
		}

		override fun onNothingSelected(parent: AdapterView<*>?) {
		}
	}
}

@InverseBindingAdapter(attribute = "selectedIndex", event = "selectedIndexValueChanged")
fun getSpinnerSelectedIndex(spinner: Spinner): Int {
	return spinner.selectedItemPosition
}

interface ItemChangeListener {
	fun onIndexChanged(index: Int)
}
