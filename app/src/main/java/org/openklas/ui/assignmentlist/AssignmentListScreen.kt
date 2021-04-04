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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.openklas.R
import org.openklas.klas.model.AssignmentEntry
import org.openklas.ui.shared.AssignmentDdayIndicator
import org.openklas.ui.shared.DueIndicator
import org.openklas.ui.shared.SubjectSelectionDialog
import org.openklas.ui.shared.bottomShadow
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun AssignmentListScreen(onClickEntry: (AssignmentEntry) -> Unit) {
	val viewModel = viewModel<AssignmentListViewModel>()
	val semesters by viewModel.semesters.observeAsState()
	val currentSemester by viewModel.currentSemester.observeAsState()
	val subjects by viewModel.subjects.observeAsState()
	val currentSubject by viewModel.currentSubject.observeAsState()

	val assignments by viewModel.assignments.observeAsState()
	val isLoading by viewModel.isLoading.observeAsState(true)

	var showSubjectDialog by remember { mutableStateOf(false) }
	AssignmentListMainLayout(
		name = currentSubject?.name ?: "",
		assignments = assignments,
		isLoading = isLoading,
		onClickEntry = onClickEntry,
		onClickSubjectChange = {
			showSubjectDialog = true
		}
	)

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
	onClickEntry: (AssignmentEntry) -> Unit,
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
			MainFrame(assignments, lazyListState, onClickEntry)
		}
	}
}

@Composable
fun MainFrame(
	assignments: Array<AssignmentEntry>?,
	lazyListState: LazyListState,
    onClickEntry: (AssignmentEntry) -> Unit
) {
	if(assignments == null) {
		// TODO display shimmer effects on data load
	}else{
		LazyColumn(state = lazyListState) {
			items(assignments, key = { it.order }) {
				AssignmentItem(it, onClickEntry)
			}
		}
	}
}

@Composable
fun AssignmentItem(entry: AssignmentEntry, onClickEntry: (AssignmentEntry) -> Unit) {
	val now = Date()
	val timeAfterDue = now.time - entry.due.time
	val daysAfterDue = timeAfterDue / TimeUnit.DAYS.toMillis(1)

	Row(modifier = Modifier
		.clickable { onClickEntry(entry) }
		.alpha(if(timeAfterDue > 0) 0.5f else 1f)
		.padding(16.dp, 20.dp)
	) {
		AssignmentDdayIndicator(
			isSubmitted = entry.isSubmitted,
			daysAfterDue = daysAfterDue
		)

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

			DueIndicator(
				start = entry.startDate,
				end = entry.due,
				dayFontSize = 18.sp,
				yearFontSize = 15.sp,
				dateVerticalMargin = 5.dp
			)
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

@Composable
fun Header(name: String, onClickSubjectChange: () -> Unit, lazyListState: LazyListState) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.bottomShadow(
				// show shadow only when scrolled
				if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)
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
			)),
			isLoading = false,
			onClickEntry = { },
			onClickSubjectChange = { }
		)
	}
}
