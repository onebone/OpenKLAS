package org.openklas.ui.assignmentlist

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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.apache.commons.lang3.time.FastDateFormat
import org.openklas.R
import org.openklas.klas.model.AssignmentEntry
import org.openklas.ui.shared.SubjectSelectionDialog
import org.openklas.ui.shared.bottomShadow
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.min

@Composable
fun AssignmentListScreen() {
	val viewModel = viewModel<AssignmentListViewModel>()
	val semesters by viewModel.semesters.observeAsState()
	val currentSemester by viewModel.currentSemester.observeAsState()
	val subjects by viewModel.subjects.observeAsState()
	val currentSubject by viewModel.currentSubject.observeAsState()

	val assignments by viewModel.assignments.observeAsState()
	val isLoading by viewModel.isLoading.observeAsState()

	var showSubjectDialog by remember { mutableStateOf(false) }
	AssignmentListMainLayout(currentSubject?.name ?: "", assignments, isLoading ?: true) {
		showSubjectDialog = true
	}

	if(showSubjectDialog) {
		SubjectSelectionDialog(semesters, currentSemester, subjects, currentSubject, onChange = { semester, subject ->
			viewModel.setSubject(subject.id)
			viewModel.setCurrentSemester(semester.id)
		}) {
			showSubjectDialog = false
		}
	}
}

@Composable
fun AssignmentListMainLayout(
	name: String,
	assignments: Array<AssignmentEntry>?,
	isLoading: Boolean,
	onClickSubjectChange: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		val lazyListState = rememberLazyListState()

		Header(name, onClickSubjectChange, lazyListState)

		if(isLoading) {
			Box(modifier = Modifier
				.fillMaxSize()
				.wrapContentSize(Alignment.Center)
			) {
				CircularProgressIndicator()
			}
		}else{
			MainFrame(assignments, lazyListState)
		}
	}
}

@Composable
fun MainFrame(assignments: Array<AssignmentEntry>?, lazyListState: LazyListState) {
	if(assignments == null) {
		// TODO display shimmer effects on data load
	}else{
		LazyColumn(
			modifier = Modifier.padding(16.dp, 0.dp),
			state = lazyListState
		) {
			item { Spacer(modifier = Modifier.padding(0.dp, 8.dp)) }

			items(assignments) {
				key(it.order) {
					AssignmentItem(it)
				}
			}
		}
	}
}

@Composable
fun AssignmentItem(entry: AssignmentEntry) {
	Row {
		Column(modifier = Modifier
			.wrapContentWidth(Alignment.Start)
			.defaultMinSize(60.dp)
		) {
			val now = Date()

			val dday = "D" +
				(if(now.after(entry.due)) '+' else '-') +
				(abs(now.time - entry.due.time) / TimeUnit.DAYS.toMillis(1))

			val color = if(entry.isSubmitted) R.color.assignment_done
				else R.color.assignment_undone

			Text(dday,
				modifier = Modifier.align(Alignment.CenterHorizontally),
				fontSize = 21.sp,
				fontWeight = FontWeight.Bold,
				color = colorResource(color)
			)

			Text(
				stringResource(
					if(entry.isSubmitted) R.string.assignment_done
					else R.string.assignment_undone
				),
				modifier = Modifier.align(Alignment.CenterHorizontally),
				fontSize = 15.sp,
				color = colorResource(color)
			)
		}

		Column(
			modifier = Modifier
				.weight(1f)
				.padding(12.dp, 0.dp)
		) {
			Text(
				entry.title,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold
			)

			val (hour, minute) = remember {
				val due = Calendar.getInstance().apply {
					time = entry.due
				}

				val hour = due.get(Calendar.HOUR_OF_DAY)
				val minute = due.get(Calendar.MINUTE)

				Pair(hour, minute)
			}

			if(!(hour == 23 && minute == 59)) {
				AssignmentDueNot2359Warning(hour, minute)
			}

			AssignmentDue(entry.startDate, entry.due)

			Spacer(modifier = Modifier.size(0.dp, 20.dp))
		}
	}
}

@Composable
fun AssignmentDueNot2359Warning(hour: Int, minute: Int) {
	Row(
		modifier = Modifier
			.padding(8.dp)
	) {
		Image(painterResource(R.drawable.ic_caution), null)

		Text(
			stringResource(R.string.assignment_due_not_23_59, hour, minute),
			modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
			color = colorResource(R.color.warn),
			fontSize = 13.sp
		)
	}
}

val DAY_FONT_SIZE = 18.sp
val YEAR_FONT_SIZE = 15.sp
val DATE_VERTICAL_MARGIN = 5.dp

@Composable
fun AssignmentDue(start: Date, end: Date) {
	val yearPaint = Paint().also { paint ->
		with(LocalDensity.current) {
			paint.asFrameworkPaint().textSize = YEAR_FONT_SIZE.toPx()
		}
	}

	val dayPaint = Paint().also { paint ->
		with(LocalDensity.current) {
			paint.asFrameworkPaint().textSize = DAY_FONT_SIZE.toPx()
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
		.assignmentDueSizing(dayPaint, yearPaint, DATE_VERTICAL_MARGIN)
	) {
		val dayFontHeight = with(dayPaint.asFrameworkPaint().fontMetrics) {
			descent - ascent
		}.toInt()

		val yearFontHeight = with(yearPaint.asFrameworkPaint().fontMetrics) {
			descent - ascent
		}.toInt()

		val radius = 8.sp.toPx()
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
				radius = strokeWidth * 1.5f,
				center = Offset(currentX, radius)
			)
		}

		drawContext.canvas.nativeCanvas.run {
			val margin = DATE_VERTICAL_MARGIN.roundToPx()

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

fun Modifier.assignmentDueSizing(
	dayPaint: Paint,
	yearPaint: Paint,
    verticalMargin: Dp
) = this.layout { measurable, constraints ->
	val width = constraints.maxWidth
	val radius = 8.sp.toPx()

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

@Composable
fun Header(name: String, onClickSubjectChange: () -> Unit, lazyListState: LazyListState) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.bottomShadow(
				// show shadow only when scrolled
				if(lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)
					0.dp
				else
					8.dp,
				colorResource(R.color.shadow_start),
				colorResource(R.color.shadow_end)
			)
			.padding(16.dp, 12.dp),
	) {
		Row {
			Text(name,
				color = colorResource(R.color.primary),
				fontSize = 24.sp,
				fontWeight = FontWeight.Bold)

			Text(
				text = stringResource(R.string.assignment_list_change_subject),
				color = colorResource(R.color.navigate),
				fontSize = 15.sp,
				modifier = Modifier
					.weight(1f)
					.wrapContentWidth(Alignment.End)
					.align(Alignment.CenterVertically)
					.clickable(onClick = onClickSubjectChange)
					.padding(4.dp)
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun AssignmentListScreenPreview() {
	MaterialTheme {
		AssignmentListMainLayout("심리학과프로파일링", arrayOf(
			AssignmentEntry(
				isExtendedPeriod = false,
				due = Calendar.getInstance().apply {
					add(Calendar.DAY_OF_MONTH, 10)
				}.time,
				isSubmitPeriod = true,
				order = 0,
				extendedDue = null,
				extendedStartDate = null,
				score = null,
				startDate = Calendar.getInstance().apply {
					add(Calendar.DAY_OF_MONTH, -15)
				}.time,
				isSubmitted = false,
				taskNumber = 0,
				title = "사이코패스 측정 후 제출",
				week = 3,
				nthOfWeek = 1
			),
			AssignmentEntry(
				isExtendedPeriod = false,
				due = Calendar.getInstance().apply {
					add(Calendar.DAY_OF_MONTH, 20)
				}.time,
				isSubmitPeriod = true,
				order = 0,
				extendedDue = null,
				extendedStartDate = null,
				score = null,
				startDate = Calendar.getInstance().apply {
					add(Calendar.DAY_OF_MONTH, -7)
				}.time,
				isSubmitted = false,
				taskNumber = 0,
				title = "보고서 제출",
				week = 3,
				nthOfWeek = 2
			)
		), false) { }
	}
}
