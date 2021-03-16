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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.openklas.R
import org.openklas.klas.model.AssignmentEntry
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun AssignmentListScreen() {
	val viewModel = viewModel<AssignmentListViewModel>()
	val subject by viewModel.subject.observeAsState()
	val assignments by viewModel.assignments.observeAsState()

	AssignmentListMainLayout(subject?.name ?: "", assignments)
}

@Composable
fun AssignmentListMainLayout(name: String, assignments: Array<AssignmentEntry>?) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Header(name)

		MainFrame(assignments)
	}
}

@Composable
fun MainFrame(assignments: Array<AssignmentEntry>?) {
	if(assignments == null) {
		// TODO display shimmer effects on data load
	}else{
		LazyColumn {
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
		Column(modifier = Modifier.weight(1f)) {
			val dday = remember {
				val now = Date()

				"D" +
					(if(now.after(entry.due)) '+' else '-') +
					((now.time - entry.due.time) / TimeUnit.DAYS.toMillis(1))
			}

			val color = remember {
				val now = Date()

				if(now.after(entry.due)) R.color.assignment_undone
				else R.color.assignment_done
			}

			Text(dday,
				modifier = Modifier.align(Alignment.CenterHorizontally),
				fontSize = 18.sp,
				fontColor = colorResource(color)
			)
			
			Text(
				stringResource(
					if(entry.isSubmitted) R.string.assignment_done
					else R.string.assignment_undone
				),
				fontColor = colorResource(color)
			)
		}
	}
}

@Composable
fun Header(name: String) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Row {
			Text(name,
				color = colorResource(R.color.primary),
				fontSize = 24.sp,
				fontWeight = FontWeight.Bold)

			Text(stringResource(R.string.post_list_change_subject),
				color = colorResource(R.color.navigate),
				fontSize = 15.sp,
				modifier = Modifier
					.weight(1f)
					.wrapContentWidth(Alignment.End)
					.align(Alignment.CenterVertically))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun AssignmentListScreenPreview() {
	MaterialTheme {
		AssignmentListMainLayout("심리학과프로파일링", arrayOf())
	}
}
