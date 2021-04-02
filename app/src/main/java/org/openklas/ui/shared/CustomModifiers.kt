package org.openklas.ui.shared

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

import androidx.annotation.FloatRange
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

fun Modifier.bottomShadow(
	height: Dp,
	shadowStart: Color,
	shadowEnd: Color
) = this.drawBehind {
	val size = size

	drawRect(
		brush = Brush.verticalGradient(
			listOf(shadowStart, shadowEnd),
			startY = size.height,
			endY = size.height + height.toPx()
		),
		topLeft = Offset(0f, size.height),
		size = Size(size.width, height.toPx()),
	)
}

fun Modifier.constrainSizeFactor(
	@FloatRange(from = 0.0, to = 1.0) minWidthFactor: Float,
	@FloatRange(from = 0.0, to = 1.0) maxWidthFactor: Float,
	@FloatRange(from = 0.0, to = 1.0) minHeightFactor: Float,
	@FloatRange(from = 0.0, to = 1.0) maxHeightFactor: Float
) = this.layout { measurable, constraints ->
	val placeable = measurable.measure(
		Constraints(
			minWidth = (constraints.maxWidth * minWidthFactor).roundToInt(),
			maxWidth = (constraints.maxWidth * maxWidthFactor).roundToInt(),
			minHeight = (constraints.maxHeight * minHeightFactor).roundToInt(),
			maxHeight = (constraints.maxHeight * maxHeightFactor).roundToInt()
		)
	)

	layout(placeable.width, placeable.height) {
		placeable.place(0, 0)
	}
}
