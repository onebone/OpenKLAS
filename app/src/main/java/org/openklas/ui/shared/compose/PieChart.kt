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

package org.openklas.ui.shared.compose

import android.graphics.Paint as NativePaint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun PieChart(
	text: String,
	modifier: Modifier = Modifier,
	entries: List<PieChartEntry>
) {
	val data = remember(entries) {
		val sum = entries.sumOf { it.value }
		entries.map {
			PieChartEntryInternal(it.name, it.value / sum, it.color)
		}
	}

	val textSize = 24.sp
	val textSizePx = with(LocalDensity.current) { textSize.toPx() }
	val paint = remember { NativePaint().apply { this.textSize = textSizePx } }
	val textWidth = remember(text, paint) { paint.measureText(text) }

	Canvas(modifier = modifier) {
		val size = size
		val axis = min(size.width, size.height)

		val padding = 10
		val chartRadius = (axis - padding * 2) / 2

		val offset = Offset(
			size.width / 2 - chartRadius,
			size.height / 2 - chartRadius
		)

		val chartSize = Size(
			chartRadius * 2, chartRadius * 2
		)

		if(data.isEmpty()) {
			drawArc(Color.Gray, 0f, 360f, false, offset, chartSize, style = Stroke(20f))
		}else{
			var acc = 0f
			data.forEach {
				val sweepAngle = (it.value * 360).toFloat()

				drawArc(it.color, acc, sweepAngle, false, offset, chartSize, style = Stroke(20f))
				acc += sweepAngle
			}
		}

		drawIntoCanvas {
			it.nativeCanvas.drawText(text, size.width / 2 - textWidth / 2, size.height / 2 + textSize.toPx() / 2, paint)
		}
	}
}

data class PieChartEntry(
	val name: String,
	val value: Double,
	val color: Color
)

private data class PieChartEntryInternal(
	val name: String,
	// sum of value should be 1.0
	val value: Double,
	val color: Color
)
