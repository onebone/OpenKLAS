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

package org.openklas.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.R
import org.openklas.klas.model.LectureSchedule
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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
	@BindingAdapter("dateShort")
	fun bindDateShort(textView: TextView, date: Date?) {
		if(date == null) return

		val context = textView.context

		val time = date.time - Date().time
		val days = abs(time / TimeUnit.DAYS.toMillis(1))
		val hours = abs(time / TimeUnit.HOURS.toMillis(1))
		val minutes = abs(time / TimeUnit.MINUTES.toMillis(1))

		val isBefore = time < 0

		textView.text = if(days > 0 && isBefore) {
			context.resources.getQuantityString(R.plurals.common_time_day_ago, days.toInt(), days)
		}else if(hours > 0) {
			context.resources.getQuantityString(
				if(isBefore) R.plurals.common_time_hours_ago
				else R.plurals.common_left_time_hour,
				hours.toInt(), hours
			)
		}else{
			if(minutes < 10) {
				context.resources.getString(
					if(isBefore) R.string.common_time_moment_ago
					else R.string.common_left_time_soon
				)
			}else{
				context.resources.getQuantityString(
					if(isBefore) R.plurals.common_time_minutes_ago
					else R.plurals.common_left_time_minute,
					minutes.toInt(), minutes
				)
			}
		}
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
	fun setTextViewScheduleText(textView: TextView, schedules: Array<LectureSchedule>?) {
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
