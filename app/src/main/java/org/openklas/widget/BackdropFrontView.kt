package org.openklas.widget

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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import org.openklas.utils.dp2px

class BackdropFrontView: FrameLayout {
	var clipWidth = dp2px(context, 16f)

	private val clipPath = Path()
	private val paint = Paint().apply {
		color = Color.WHITE
	}

	constructor(context: Context): super(context)
	constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)

	init {
		setWillNotDraw(false)
	}

	override fun onDraw(canvas: Canvas) {
		canvas.clipPath(clipPath.apply {
			reset()
			moveTo(clipWidth, 0f)
			lineTo(width.toFloat(), 0f)
			lineTo(width.toFloat(), height.toFloat())
			lineTo(0f, height.toFloat())
			lineTo(0f, clipWidth)
			close()
		})

		canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

		super.onDraw(canvas)
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		return false
	}
}
