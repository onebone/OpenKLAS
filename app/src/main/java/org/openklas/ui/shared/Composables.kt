package org.openklas.ui.shared

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.apache.commons.lang3.time.FastDateFormat
import org.openklas.R
import java.util.Date
import java.util.Locale

@Composable
fun DueIndicator(
	start: Date,
	end: Date,
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

	val yearFormatter = FastDateFormat.getInstance("yyyy", Locale.getDefault())
	val dayFormatter = FastDateFormat.getInstance("MMM dd", Locale.getDefault())

	val now = Date()

	val isBeforeStart = now < start
	val isBeforeEnd = now < end

	val periodRatio = (now.time.toFloat() - start.time) / (end.time - start.time)

	val startColor = colorResource(
		if(isBeforeStart) R.color.assignment_due_in_period
		else R.color.assignment_due_not_in_period
	)

	val endColor = colorResource(
		if(isBeforeEnd) R.color.assignment_due_in_period
		else R.color.assignment_due_not_in_period
	)

	val currentColor = colorResource(R.color.assignment_due_current)

	Canvas(modifier = Modifier
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
