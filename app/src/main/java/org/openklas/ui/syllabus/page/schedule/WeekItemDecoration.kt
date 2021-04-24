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

package org.openklas.ui.syllabus.page.schedule

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.openklas.R
import org.openklas.utils.dp2px

class WeekItemDecoration(
	private val color: Int
): RecyclerView.ItemDecoration() {
	private val paint = Paint()

	override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		val itemCount = parent.adapter?.itemCount ?: 0
		val childCount = parent.childCount

		val paint = paint.also {
			it.color = color
			it.strokeWidth = dp2px(parent.context, 2.2f)
		}

		repeat(childCount) { childIndex ->
			val child = parent.getChildAt(childIndex)
			val circle = child.findViewById<View>(R.id.v_circle)

			val childParams = child.layoutParams as RecyclerView.LayoutParams

			val x = (circle.left + circle.right) / 2f

			val position = parent.getChildAdapterPosition(child)
			if(position < itemCount - 1) {
				val bottom = child.bottom + childParams.bottomMargin
				c.drawLine(x, child.top.toFloat() + circle.bottom, x, bottom.toFloat(), paint)
			}

			if(position > 0) {
				val top = child.top + childParams.topMargin
				c.drawLine(x, top.toFloat(), x, child.top.toFloat() + circle.top, paint)
			}
		}
	}
}
