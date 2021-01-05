package org.openklas.binding

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

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.R
import java.util.Date
import java.util.concurrent.TimeUnit


object BindAdapter {
	@JvmStatic
	@BindingAdapter("items")
	fun <T> bindItems(recyclerView: RecyclerView, list: Array<T>?) {
		if(recyclerView.adapter is ListAdapter<*, *>) {
			@Suppress("UNCHECKED_CAST")
			(recyclerView.adapter as ListAdapter<T, *>).submitList(list?.toList())
		}
	}

	@JvmStatic
	@BindingAdapter("tint")
	fun bindImageTint(imageView: ImageView, @ColorInt color: Int) {
		imageView.setColorFilter(color)
	}

	@JvmStatic
	@BindingAdapter("endDate")
	fun bindEndDate(textView: TextView, endDate: Date?) {
		endDate?.let {
			val time = it.time - Date().time

			val context = textView.context

			val hoursLeft = time / TimeUnit.HOURS.toMillis(1)
			textView.text = if(hoursLeft > 0) {
				context.resources.getQuantityString(R.plurals.home_left_time_hour, hoursLeft.toInt(), hoursLeft)
			}else{
				val minutesLeft = time / TimeUnit.MINUTES.toMillis(1)

				if(minutesLeft < 10) {
					context.resources.getString(R.string.home_left_time_soon)
				}else{
					context.resources.getQuantityString(
						R.plurals.home_left_time_minute,
						minutesLeft.toInt(),
						minutesLeft
					)
				}
			}
		}
	}
}
