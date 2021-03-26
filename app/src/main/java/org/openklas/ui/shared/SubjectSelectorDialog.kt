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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.openklas.R
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import kotlin.math.roundToInt

const val STATE_SEMESTER = 0
const val STATE_SUBJECT = 1

@Composable
fun SubjectSelectionDialog(
	semesters: Array<Semester>?,
	currentSemester: Semester?,
	subjects: Array<BriefSubject>?,
	currentSubject: BriefSubject?,
	onChange: (Semester, BriefSubject) -> Unit,
	onDismissRequest: () -> Unit
) {
	var state by remember { mutableStateOf(STATE_SUBJECT) }

	var semesterSelection by remember { mutableStateOf(currentSemester) }
	var subjectSelection by remember { mutableStateOf(currentSubject) }

	var subjectList by remember { mutableStateOf(subjects) }

	Dialog(
		onDismissRequest = onDismissRequest
	) {
		if(semesterSelection == null || state == STATE_SEMESTER)
			SemesterSelectionDialogContent(semesters = semesters, currentSemester = semesterSelection, onSemesterSelection = {
				semesterSelection = it

				subjectList = it.subjects
				subjectSelection = null

				state = STATE_SUBJECT
			}) {
				state = STATE_SUBJECT
			}
		else
			SubjectSelectionDialogContent(subjects = subjectList, currentSubject = subjectSelection, onSubjectSelection = {
				subjectSelection = it

				if(semesterSelection != null && subjectSelection != null) {
					onChange(semesterSelection!!, subjectSelection!!)
					onDismissRequest()
				}
			}) {
				state = STATE_SEMESTER
			}
	}
}

@Composable
fun SemesterSelectionDialogContent(
	semesters: Array<Semester>?,
	currentSemester: Semester?,
	onSemesterSelection: (Semester) -> Unit,
	onClickChangeSubject: () -> Unit
) {
	Surface(
		modifier = Modifier
			.background(MaterialTheme.colors.surface)
	) {
		Column(modifier = Modifier
			.padding(8.dp)
			.constrainSizeFactor(0.8f, 0.9f, 0f, 0.6f)
		) {
			Row {
				Text(
					text = stringResource(R.string.common_subject_dialog_title),
					modifier = Modifier
						.wrapContentWidth(Alignment.CenterHorizontally)
						.padding(4.dp),
					fontWeight = Bold
				)

				Text(
					text = stringResource(R.string.common_subject_dialog_title),
					modifier = Modifier
						.wrapContentWidth(Alignment.End)
						.padding(4.dp)
						.clickable(onClick = onClickChangeSubject),
					color = colorResource(R.color.navigate)
				)
			}

			semesters?.let { semesters ->
				LazyColumn {
					items(semesters) {
						if(it.id == currentSemester?.id) {
							Text(
								text = it.label,
								modifier = Modifier.clickable(onClick = onClickChangeSubject),
								color = colorResource(R.color.selected)
							)
						}else{
							Text(
								text = it.label,
								modifier = Modifier.clickable { onSemesterSelection(it) }
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun SubjectSelectionDialogContent(
	subjects: Array<BriefSubject>?,
	currentSubject: BriefSubject?,
	onSubjectSelection: (BriefSubject) -> Unit,
	onClickChangeSemester: () -> Unit
) {
	Surface(
		modifier = Modifier
			.background(MaterialTheme.colors.surface)
	) {
		Column(modifier = Modifier
			.padding(8.dp)
			.constrainSizeFactor(0.8f, 0.9f, 0f, 0.6f)
		) {
			Text(
				text = stringResource(R.string.common_subject_dialog_title),
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(4.dp),
				fontWeight = Bold
			)

			subjects?.let { subjects ->
				LazyColumn {
					items(subjects) {
						if(it.id == currentSubject?.id) {
							Text(
								text = it.name,
								modifier = Modifier.clickable { onSubjectSelection(it) },
								color = colorResource(R.color.selected),
								fontWeight = Bold
							)
						}else{
							Text(
								text = it.name,
								modifier = Modifier.clickable { onSubjectSelection(it) }
							)
						}
					}
				}
			}
		}
	}
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
