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

package org.openklas.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.openklas.R
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester

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
			SemesterSelectionDialogContent(
				semesters = semesters,
				currentSemester = semesterSelection,
				onSemesterSelection = {
					semesterSelection = it

					subjectList = it.subjects
					subjectSelection = null

					state = STATE_SUBJECT
				},
				onClickChangeSubject = {
					state = STATE_SUBJECT
				}
			)
		else
			SubjectSelectionDialogContent(
				subjects = subjectList,
				currentSubject = subjectSelection,
				onSubjectSelection = {
					subjectSelection = it

					if(semesterSelection != null && subjectSelection != null) {
						onChange(semesterSelection!!, subjectSelection!!)
						onDismissRequest()
					}
				},
				onClickChangeSemester = {
					state = STATE_SEMESTER
				}
			)
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
			.constrainSizeFactor(0.8f, 0.9f, 0f, 0.6f)
		) {
			DialogHeader(
				title = stringResource(R.string.common_semester_dialog_title),
				buttonAlignment = Alignment.CenterEnd,
				onClickButton = onClickChangeSubject
			) {
				Text(
					text = stringResource(R.string.common_subject_dialog_title),
					color = colorResource(R.color.primary)
				)

				Icon(
					painterResource(R.drawable.ic_arrow),
					contentDescription = null,
					modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
					tint = colorResource(R.color.primary)
				)
			}

			if(semesters != null) {
				SelectionItems(
					items = semesters,
					selectedItem = { it.id == currentSemester?.id },
					displayName = { it.label },
					onClick = { onSemesterSelection(it) }
				)
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
			.constrainSizeFactor(0.8f, 0.9f, 0f, 0.6f)
		) {
			DialogHeader(
				title = stringResource(R.string.common_subject_dialog_title),
				buttonAlignment = Alignment.CenterStart,
				onClickButton = onClickChangeSemester
			) {
				Icon(
					painterResource(R.drawable.ic_arrow_back),
					contentDescription = null,
					modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp),
					tint = colorResource(R.color.primary)
				)

				Text(
					text = stringResource(R.string.common_semester_dialog_title),
					color = colorResource(R.color.primary)
				)
			}

			if(subjects != null) {
				SelectionItems(
					items = subjects,
					selectedItem = { it.id == currentSubject?.id },
					displayName = { it.name },
					onClick = { onSubjectSelection(it) }
				)
			}
		}
	}
}

@Composable
fun DialogHeader(
	title: String,
	buttonAlignment: Alignment,
	onClickButton: () -> Unit,
	button: @Composable () -> Unit
) {
	Box(modifier = Modifier
		.padding(4.dp)
		.fillMaxWidth()
	) {
		Text(
			text = title,
			modifier = Modifier.align(Alignment.Center),
			fontWeight = Bold,
			fontSize = 18.sp
		)

		Row(modifier = Modifier
			.align(buttonAlignment)
			.padding(0.dp, 0.dp, 4.dp, 0.dp)
			.clickable(onClick = onClickButton)
			.padding(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			button()
		}
	}
}

@Composable
fun<T> SelectionItems(
	items: Array<T>,
	selectedItem: (T) -> Boolean,
	displayName: (T) -> String,
	onClick: (T) -> Unit
) {
	LazyColumn {
		items(items) {
			Text(
				text = displayName(it),
				modifier = Modifier
					.fillMaxWidth()
					.clickable(onClick = { onClick(it) })
					.padding(24.dp, 12.dp),
				color =
					if(selectedItem(it)) colorResource(R.color.selected)
					else Color.Unspecified,
				fontSize = 15.sp
			)
		}
	}
}
