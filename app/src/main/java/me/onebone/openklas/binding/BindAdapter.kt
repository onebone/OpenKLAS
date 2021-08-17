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

package me.onebone.openklas.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.LectureSchedule
import me.onebone.openklas.utils.dateToShortString
import java.time.ZonedDateTime

object BindAdapter {
	@JvmStatic
	@BindingAdapter("items")
	fun <T> bindItems(recyclerView: RecyclerView, list: List<T>?) {
		if(recyclerView.adapter is ListAdapter<*, *>) {
			@Suppress("UNCHECKED_CAST")
			(recyclerView.adapter as ListAdapter<T, *>).submitList(list)
		}
	}

	@JvmStatic
	@BindingAdapter("tint")
	fun bindImageTint(imageView: ImageView, @ColorInt color: Int) {
		imageView.setColorFilter(color)
	}

	@JvmStatic
	@BindingAdapter("dateShort")
	fun bindDateShort(textView: TextView, date: ZonedDateTime?) {
		if(date == null) return

		textView.text = dateToShortString(textView.context, date, ZonedDateTime.now())
	}

	@JvmStatic
	@BindingAdapter("drawableStartTint")
	fun setTextViewDrawableStartTint(textView: TextView, color: Int) {
		val drawables = textView.compoundDrawablesRelative
		val startDrawable = drawables[0]
		if(startDrawable != null) {
			val clone = startDrawable.mutate().apply {
				DrawableCompat.setTint(this, color)
			}

			textView.setCompoundDrawablesRelative(clone, drawables[1], drawables[2], drawables[3])
		}
	}

	@JvmStatic
	@BindingAdapter("scheduleText")
	fun setTextViewScheduleText(textView: TextView, schedules: List<LectureSchedule>?) {
		if(schedules == null) return

		val resources = textView.context.resources
		val nullClassroom = resources.getString(R.string.classroom_undefined)

		textView.text = schedules.joinToString(" / ", transform = {
			resources.getString(R.string.lecture_schedule_format)
				.format(it.dayLabel, it.periods.joinToString(","), it.classroom ?: nullClassroom)
		})
	}

	@JvmStatic
	@BindingAdapter("html")
	fun setTextViewHtml(textView: TextView, html: String?) {
		if(html == null) return

		textView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
	}
}
