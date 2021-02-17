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
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import org.openklas.R
import org.openklas.utils.dp2px
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class PieChartView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
	private var totalValue = 0
	var entries = arrayOf<Entry>()
		set(value) {
			field = value
			totalValue = entries.sumBy { it.value }

			invalidate()
		}

	var textSize: Int = 0
	var textMargin: Int = 0
		set(value) {
			field = value

			if(value < 0) throw IllegalArgumentException("textMargin cannot be negative")
		}
	var strokeWidth: Int = 0

	private var centerX = 0f
	private var centerY = 0f
	private var radius = 0f
	private var centerAngle = 0f

	private val paint = Paint().apply {
		style = Paint.Style.STROKE
	}

	private val textPaint = Paint()

	private val bounds = RectF()

	init {
		setWillNotDraw(false)

		context.obtainStyledAttributes(attrs, R.styleable.PieChartView).apply {
			textSize = getDimensionPixelSize(R.styleable.PieChartView_textSize, dp2px(context, 15f).toInt())
			textMargin = getDimensionPixelSize(R.styleable.PieChartView_textMargin, dp2px(context, 8f).toInt())
			strokeWidth = getDimensionPixelSize(R.styleable.PieChartView_strokeWidth, dp2px(context, 12f).toInt())
		}.recycle()
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)

		centerX = w / 2f
		centerY = h / 2f

		radius = min(
			width - paddingLeft - paddingRight,
			height - paddingTop - paddingBottom - textSize * 2 - textMargin * 4 - strokeWidth * 2
		) / 2f

		bounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

		centerAngle = Math.toDegrees(acos((radius + strokeWidth) / (radius + textMargin + strokeWidth)).toDouble()).toFloat()
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		paint.strokeWidth = strokeWidth.toFloat()
		textPaint.textSize = textSize.toFloat()

		var acc = 0
		canvas.apply {
			entries.forEach {
				if(it.value <= 0) return@forEach

				val current = acc.toFloat() / totalValue
				val sweep = it.value.toFloat() / totalValue

				paint.color = it.color
				drawArc(bounds, current * 360, sweep * 360, false, paint)

				val center = (current + sweep / 2) * 360

				val text = "${it.label} (${(sweep * 100).roundToInt()}%)"
				val textWidth = textPaint.measureText(text)
				textPaint.color = it.color

				val radian = Math.toRadians(center.toDouble())
				val pointX = centerX + (radius + textMargin + strokeWidth / 2f) * cos(radian).toFloat()
				val pointY = centerY + (radius + textMargin + strokeWidth / 2f) * sin(radian).toFloat()

				val a = centerAngle
				val b = 90 - 2 * a

				val textX = when(center) {
					in 0f..(a+b), in (7*a+3*b)..(8*a+4*b) -> pointX // constraint left
					in (a+b)..(3*a+b), in (5*a+3*b)..(7*a+3*b) -> pointX - textWidth / 2f // constraint center
					in (3*a+b)..(5*a+3*b) -> pointX - textWidth // constraint right
					else -> throw IllegalStateException("illegal center angle: $center")
				}

				val textY = when(center) {
					in a..(3*a+2*b) -> pointY + textSize // constraint top
					in 0f..a, in (3*a+2*b)..(5*a+2*b), in (7*a+4*b)..(8*a+4*b) -> pointY + textSize / 2 // constraint center
					in (5*a+2*b)..(7*a+4*b) -> pointY // constraint bottom
					else -> throw IllegalStateException("illegal center angle: $center")
				}

				drawText(text, textX, textY, textPaint)

				acc += it.value
			}
		}
	}

	data class Entry(
		val label: String,
		val value: Int,
		val color: Int
	)
}
