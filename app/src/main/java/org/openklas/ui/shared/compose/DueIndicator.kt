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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.apache.commons.lang3.time.FastDateFormat
import org.openklas.R
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun DueIndicator(
	modifier: Modifier = Modifier,
	start: ZonedDateTime,
	end: ZonedDateTime,
	dayFontSize: TextUnit,
	yearFontSize: TextUnit,
	dateVerticalMargin: Dp
) {
	val yearPaint = Paint().also { paint ->
		with(LocalDensity.current) {
			paint.asFrameworkPaint().textSize = yearFontSize.toPx()
		}
	}

	val dayPaint = Paint().also { paint ->
		with(LocalDensity.current) {
			paint.asFrameworkPaint().textSize = dayFontSize.toPx()
		}
	}

	val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
	val dayFormatter = DateTimeFormatter.ofPattern("MMM dd")

	val now = ZonedDateTime.now()

	val isBeforeStart = now < start
	val isBeforeEnd = now < end

	val periodRatio = Duration.between(start, now).nano.toFloat() / Duration.between(start, end).nano

	val startColor = colorResource(
		if(isBeforeStart) R.color.assignment_due_in_period
		else R.color.assignment_due_not_in_period
	)

	val endColor = colorResource(
		if(isBeforeEnd) R.color.assignment_due_in_period
		else R.color.assignment_due_not_in_period
	)

	val currentColor = colorResource(R.color.assignment_due_current)

	Canvas(modifier = modifier
		.dueIndicatorSizing(dayPaint, yearPaint, dateVerticalMargin)
	) {
		val dayFontHeight = with(dayPaint.asFrameworkPaint().fontMetrics) {
			descent - ascent
		}.toInt()

		val yearFontHeight = with(yearPaint.asFrameworkPaint().fontMetrics) {
			descent - ascent
		}.toInt()

		val radius = 8.dp.toPx()
		val strokeWidth = radius / 4

		val currentX = (radius * 2 + (size.width - 4 * radius) * periodRatio)
			.coerceIn(radius * 2, size.width - 2 * radius)

		drawLine(
			color = startColor,
			start = Offset(radius * 2, radius),
			end = Offset(currentX, radius),
			strokeWidth = strokeWidth
		)

		drawLine(
			color = endColor,
			start = Offset(currentX, radius),
			end = Offset(size.width - radius * 2, radius),
			strokeWidth = strokeWidth
		)

		drawCircle(
			color = startColor,
			style = Stroke(strokeWidth),
			radius = radius,
			center = Offset(radius, radius)
		)

		drawCircle(
			color = endColor,
			style = Stroke(strokeWidth),
			radius = radius,
			center = Offset(size.width - radius, radius)
		)

		if(!isBeforeStart && isBeforeEnd) {
			// mark current time
			drawCircle(
				color = currentColor,
				radius = strokeWidth * 2f,
				center = Offset(currentX, radius)
			)
		}

		drawContext.canvas.nativeCanvas.run {
			val margin = dateVerticalMargin.roundToPx()

			drawText(
				dayFormatter.format(start),
				0f, radius * 2 + dayFontHeight + margin,
				dayPaint.apply {
					color = startColor
				}.asFrameworkPaint()
			)

			drawText(
				yearFormatter.format(start),
				0f, radius * 2 + dayFontHeight + yearFontHeight + margin,
				yearPaint.apply {
					color = startColor
				}.asFrameworkPaint()
			)

			val endYearString = yearFormatter.format(end)
			val endDayString = dayFormatter.format(end)

			val endDayWidth = dayPaint.asFrameworkPaint().measureText(endDayString)
			val endYearWidth = yearPaint.asFrameworkPaint().measureText(endYearString)

			drawText(
				dayFormatter.format(end),
				size.width - endDayWidth, radius * 2 + dayFontHeight + margin,
				dayPaint.apply {
					color = endColor
				}.asFrameworkPaint()
			)

			drawText(
				yearFormatter.format(end),
				size.width - endYearWidth, radius * 2 + dayFontHeight + yearFontHeight + margin,
				yearPaint.apply {
					color = endColor
				}.asFrameworkPaint()
			)
		}
	}
}

@Composable
fun AssignmentDdayIndicator(
	modifier: Modifier = Modifier,
	isSubmitted: Boolean,
	durationAfterDue: Duration,
	blinkIfImpending: Boolean = true
) {
	val days = durationAfterDue.toDays()

	val alpha by
		if(blinkIfImpending && -1L <= days && durationAfterDue.isNegative) {
			blinkTransition(fromAlpha = 0.3f)
		}else{
			remember { mutableStateOf(1f) }
		}

	Column(modifier = modifier
		.defaultMinSize(50.dp)
		.alpha(alpha)
	) {
		val dday = "D" +
				(if(durationAfterDue.isNegative) '-' else '+') + days.absoluteValue

		val color = if(isSubmitted) R.color.assignment_done
		else R.color.assignment_undone

		Text(dday,
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontSize = 21.sp,
			fontWeight = FontWeight.Bold,
			color = colorResource(color)
		)

		Text(
			stringResource(
				if(isSubmitted) R.string.assignment_done
				else R.string.assignment_undone
			),
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontSize = 15.sp,
			color = colorResource(color)
		)
	}
}

@Composable
fun DueNot2359Warning(hour: Int, minute: Int) {
	Row(
		modifier = Modifier
			.padding(8.dp)
	) {
		Image(painterResource(R.drawable.ic_caution), null)

		Text(
			stringResource(R.string.assignment_due_not_23_59, hour, minute),
			color = colorResource(R.color.warn),
			fontSize = 13.sp
		)
	}
}

fun Modifier.dueIndicatorSizing(
	dayPaint: Paint,
	yearPaint: Paint,
	verticalMargin: Dp
) = this.layout { measurable, constraints ->
	val width = constraints.maxWidth
	val radius = 8.dp.toPx()

	val dayFontHeightPixel = with(dayPaint.asFrameworkPaint().fontMetrics) {
		descent - ascent
	}.toInt()

	val yearFontHeightPixel = with(yearPaint.asFrameworkPaint().fontMetrics) {
		descent - ascent
	}.toInt()

	val height = (radius * 2 + dayFontHeightPixel + yearFontHeightPixel + verticalMargin.roundToPx())
		.coerceIn(constraints.minHeight.toFloat(), constraints.maxHeight.toFloat()).toInt()

	val placeable = measurable.measure(
		Constraints(width, width, height, height)
	)

	layout(placeable.width, placeable.height) {
		placeable.place(0, 0)
	}
}
