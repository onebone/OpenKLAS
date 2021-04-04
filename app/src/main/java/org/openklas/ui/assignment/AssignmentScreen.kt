package org.openklas.ui.assignment

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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.openklas.R
import org.openklas.klas.model.Assignment
import org.openklas.klas.model.Attachment
import org.openklas.klas.model.BriefSubject
import org.openklas.ui.shared.AssignmentDdayIndicator
import org.openklas.ui.shared.AttachmentList
import org.openklas.ui.shared.DueIndicator
import org.openklas.ui.shared.DueNot2359Warning
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun AssignmentScreen(onDownloadAttachment: (Attachment) -> Unit) {
	val viewModel = viewModel<AssignmentViewModel>()

	val subject by viewModel.currentSubject.observeAsState()
	val assignment by viewModel.assignment.observeAsState()
	val attachments by viewModel.attachments.observeAsState()

	MainFrame(
		subject = subject,
		assignment = assignment,
		attachments = attachments,
		onDownloadAttachment = onDownloadAttachment
	)
}

@Composable
fun MainFrame(
	subject: BriefSubject?,
	assignment: Assignment?,
	attachments: Array<Attachment>?,
	onDownloadAttachment: (Attachment) -> Unit
) {
	Surface(modifier = Modifier
		.background(MaterialTheme.colors.background)
	) {
		Column {
			Header(subject = subject, title = assignment?.description?.title)

			DueFrame(assignment = assignment?.description)

			attachments?.let {
				AttachmentList(
					attachments = it,
					onClickEntry = { attachment ->
						onDownloadAttachment(attachment)
					}
				)
			}

			AssignmentDescriptionBody(assignment = assignment?.description)
		}
	}
}

@Composable
fun Header(subject: BriefSubject?, title: String?) {
	Column(modifier = Modifier
		.padding(12.dp)
	) {
		Text(
			text = subject?.name ?: "",
			color = colorResource(R.color.primary),
			fontSize = 15.sp
		)

		Text(
			text = title ?: "",
			fontWeight = FontWeight.Bold,
			fontSize = 21.sp
		)
	}
}

@Composable
fun DueFrame(assignment: Assignment.Description?) {
	if(assignment == null) {
		// TODO show load indicator such as shimmer
		return
	}

	val now = Date()
	val daysAfterDue = (now.time - assignment.due.time) / TimeUnit.DAYS.toMillis(1)

	Row(modifier = Modifier
		.padding(12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		AssignmentDdayIndicator(
			modifier = Modifier.padding(8.dp, 0.dp),
			isSubmitted = assignment.isSubmitted,
			daysAfterDue = daysAfterDue
		)

		Column(modifier = Modifier
			.weight(1f)
			.padding(8.dp, 0.dp)
		) {
			DueIndicator(
				start = assignment.startDate,
				end = assignment.due,
				dayFontSize = 18.sp,
				yearFontSize = 15.sp,
				dateVerticalMargin = 5.dp
			)

			val (hour, minute) = remember {
				val calendar = Calendar.getInstance().apply {
					time = assignment.due
				}

				Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
			}

			if(!(hour == 23 && minute == 59)) {
				DueNot2359Warning(hour = hour, minute = minute)
			}
		}
	}
}

@Composable
fun AssignmentDescriptionBody(assignment: Assignment.Description?) {
	if(assignment == null) {
		// TODO loading indicator such as shimmer
		return
	}

	SelectionContainer {
		Text(
			text = assignment.content, // TODO handle html elements
			modifier = Modifier.padding(12.dp)
		)
	}
}
