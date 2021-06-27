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

package org.openklas.ui.assignmentlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Duration
import java.time.ZonedDateTime
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState
import org.openklas.R
import org.openklas.klas.model.AssignmentEntry
import org.openklas.ui.shared.compose.AssignmentDdayIndicator
import org.openklas.ui.shared.compose.DueIndicator
import org.openklas.ui.shared.compose.DueNot2359Warning
import org.openklas.ui.shared.SubjectSelectionDialog
import org.openklas.ui.shared.compose.bottomShadow

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
	Surface(modifier = Modifier
		.background(MaterialTheme.colors.background)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
		) {
			val lazyListState = rememberLazyListState()

			val collapsingToolbarState = rememberCollapsingToolbarState()

			val state = rememberCollapsingToolbarScaffoldState()
			CollapsingToolbarScaffold(
				modifier = Modifier.fillMaxWidth(),
				state = state,
				scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
				toolbarModifier = Modifier.bottomShadow(
					// show shadow only when scrolled
					if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)
						0.dp
					else
						8.dp,
					colorResource(R.color.shadow_start),
					colorResource(R.color.shadow_end)
				),
				toolbar = {
					Row(
						modifier = Modifier
							.road(Alignment.CenterStart, Alignment.Center)
					) {
						Text(
							text = name,
							modifier = Modifier
								.padding(8.dp)
								.align(Alignment.CenterVertically),
							color = colorResource(R.color.primary),
							fontSize = 24.sp,
							fontWeight = FontWeight.Bold
						)

						Text(
							text = stringResource(R.string.assignment_list_change_subject),
							modifier = Modifier
								.align(Alignment.CenterVertically)
								.graphicsLayer {
									alpha = collapsingToolbarState.progress
								}
								.clickable(onClick = onClickSubjectChange)
								.padding(4.dp),
							color = colorResource(R.color.navigate),
							fontSize = 15.sp
						)
					}

					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(150.dp)
					)
				}
			) {
				if(isLoading) {
					Box(modifier = Modifier
						.fillMaxSize()
						.wrapContentSize(Alignment.Center)
					) {
						CircularProgressIndicator()
					}
				}else{
					MainFrame(
						assignments = assignments,
						lazyListState = lazyListState,
						onClickEntry = onClickEntry
					)
				}
			}
		}
	}
}

@Composable
fun MainFrame(
	modifier: Modifier = Modifier,
	assignments: Array<AssignmentEntry>?,
	lazyListState: LazyListState,
    onClickEntry: (AssignmentEntry) -> Unit
) {
	if(assignments == null) {
		// TODO display shimmer effects on data load
	}else{
		LazyColumn(
			modifier = modifier
				.fillMaxSize(),
			state = lazyListState
		) {
			items(assignments, key = { it.order }) {
				AssignmentItem(it, onClickEntry)
			}
		}
	}
}

@Composable
fun AssignmentItem(entry: AssignmentEntry, onClickEntry: (AssignmentEntry) -> Unit) {
	val now = ZonedDateTime.now()
	val durationAfterDue = Duration.between(now, entry.due)

	Row(modifier = Modifier
		.clickable { onClickEntry(entry) }
		.alpha(if(!durationAfterDue.isNegative) 0.5f else 1f)
		.padding(16.dp, 20.dp)
	) {
		AssignmentDdayIndicator(
			isSubmitted = entry.isSubmitted,
			durationAfterDue = durationAfterDue
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

			val hour = entry.due.hour
			val minute = entry.due.minute

			if(!(hour == 23 && minute == 59)) {
				DueNot2359Warning(hour, minute)
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

@Preview(showBackground = true)
@Composable
fun AssignmentListScreenPreview() {
	MaterialTheme {
		AssignmentListMainLayout("심리학과프로파일링", arrayOf(
			AssignmentEntry(
				isExtendedPeriod = false,
				due = ZonedDateTime.now() + Duration.ofDays(10),
				isSubmitPeriod = true,
				order = 0,
				extendedDue = null,
				extendedStartDate = null,
				score = null,
				startDate = ZonedDateTime.now() - Duration.ofDays(10),
				isSubmitted = false,
				taskNumber = 0,
				title = "사이코패스 측정 후 제출",
				week = 3,
				nthOfWeek = 1
			),
			AssignmentEntry(
				isExtendedPeriod = false,
				due = ZonedDateTime.now() + Duration.ofDays(20),
				isSubmitPeriod = true,
				order = 0,
				extendedDue = null,
				extendedStartDate = null,
				score = null,
				startDate = ZonedDateTime.now() - Duration.ofDays(7),
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
