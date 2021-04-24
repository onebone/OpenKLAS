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

package org.openklas.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import org.openklas.R
import org.openklas.klas.model.Timetable
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class TimetableView: GridLayout {
	private var minPeriods = 5
	private var cellMinHeight: Int = 30

	// TODO use Adapter instead of directly feeding domain object here
	var timetable: Timetable? = null
		set(value) {
			field = value?.apply {
				refreshTimetable(this)
			}
		}

	companion object {
		// The time of e-learning sessions are represented with number larger than 11(=FIXED_PERIODS)
		// If specified time of timetable entry is smaller than or equal to 11, it implies that
		// the session is not e-learning.
		const val FIXED_PERIODS = 11
	}

	constructor(context: Context): this(context, null, 0)

	constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

	constructor(context: Context, attrs: AttributeSet?, defAttrStyle: Int): super(context, attrs, defAttrStyle) {
		context.obtainStyledAttributes(attrs, R.styleable.TimetableView).apply {
			minPeriods = getInt(R.styleable.TimetableView_minPeriods, 5)
			cellMinHeight = getDimensionPixelSize(R.styleable.TimetableView_cellMinHeight, 30)
		}.recycle()
	}

	private fun refreshTimetable(timetable: Timetable) {
		this.removeAllViews()

		val periodSet = mutableSetOf<Int>()
		val daySet = mutableSetOf<Int>()

		timetable.entries.forEach {
			daySet += it.day
			periodSet +=
				if(it.time <= FIXED_PERIODS) (min(1, it.time) until it.time + it.length)
				else it.time until it.time + it.length
		}

		val periodMap = mutableMapOf<Int, Int>() // <time, row>
		periodSet.sorted().forEachIndexed { row, time ->
			periodMap[time] = row + 1
		}

		rowCount = max(minPeriods, periodSet.size) + 1
		columnCount = max(5, daySet.maxOrNull() ?: 5) + 1 // minimum 5 (Mon, ..., Fri)

		addView(TextView(context), LayoutParams().apply {
			rowSpec = spec(0)
			columnSpec = spec(0)
		})

		periodSet.sorted().forEachIndexed { row, time ->
			addView(TextView(context).apply {
				text = time.toString()
				gravity = Gravity.CENTER
				minHeight = cellMinHeight
			}, LayoutParams().apply {
				width = LayoutParams.WRAP_CONTENT
				height = LayoutParams.WRAP_CONTENT

				rowSpec = spec(row + 1, 1f)
				columnSpec = spec(0)
			})
		}

		val dayNames = resources.obtainTypedArray(R.array.short_day_names)

		for(day in 0 until columnCount-1) {
			addView(TextView(context).apply {
				text = dayNames.getString(day)
				gravity = Gravity.CENTER
			}, LayoutParams().apply {
				width = LayoutParams.WRAP_CONTENT
				height = LayoutParams.WRAP_CONTENT

				rowSpec = spec(0)
				columnSpec = spec(day + 1, 1f)
			})
		}

		dayNames.recycle()

		val cellColors = resources.obtainTypedArray(R.array.timetable_colors)
		val colorLength = cellColors.length()

		val inflater = LayoutInflater.from(context)
		timetable.entries.forEach {
			if(it.day < 1)
				throw IllegalArgumentException("timetable entry cannot have day smaller than 1")

			val cell = inflater.inflate(R.layout.timetable_cell, this, false)
			cell.setBackgroundColor(cellColors.getColor((it.printSeq - 1) % colorLength, Color.WHITE))

			val tvName = cell.findViewById<TextView>(R.id.tv_subject_name)
			val tvClassroom = cell.findViewById<TextView>(R.id.tv_subject_classroom)

			tvName.text = it.subjectName
			tvClassroom.text = it.classroom

			addView(cell, LayoutParams().apply {
				width = 0
				height = 0

				rowSpec = spec(periodMap[it.time]!!, it.length, 1f)
				columnSpec = spec(it.day, 1f)
			})
		}

		cellColors.recycle()
	}
}
