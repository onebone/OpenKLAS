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

package org.openklas.ui.grade

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.openklas.R
import org.openklas.klas.model.CreditStatus
import org.openklas.klas.model.SchoolRegister
import org.openklas.klas.model.SemesterGrade
import org.openklas.utils.ViewResource
import org.openklas.utils.getGpa

private val MajorColumnWidth = 50.dp
private val OverallColumnWidth = 50.dp

@Composable
fun GradeScreen(
	viewModel: GradeViewModel
) {
	val grades by viewModel.grades.collectAsState(initial = ViewResource.Loading())
	val creditStatus by viewModel.creditStatus.collectAsState(initial = ViewResource.Loading())
	val schoolRegister by viewModel.schoolRegister.collectAsState(initial = ViewResource.Loading())
	var semester by remember { mutableStateOf<SemesterGrade?>(null) }

	val mainVerticalScrollState = rememberScrollState()

	MaterialTheme {
		Surface {
			if(semester == null) {
				GradeOverviewLayout(
					grades = grades,
					schoolRegister = schoolRegister,
					creditStatus = creditStatus,
					onSemesterClick = {
						semester = it
					},
					scrollState = mainVerticalScrollState
				)
			}else{
				GradeSemesterFrame(grades = semester!!)
			}
		}
	}

	BackHandler(enabled = semester != null) {
		semester = null
	}
}

@Composable
fun GradeOverviewLayout(
	grades: ViewResource<List<SemesterGrade>>,
	schoolRegister: ViewResource<SchoolRegister>,
	creditStatus: ViewResource<CreditStatus>,
	onSemesterClick: (SemesterGrade) -> Unit,
	scrollState: ScrollState
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(scrollState),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		SchoolRegisterFrame(schoolRegister = schoolRegister)
		CreditStatusFrame(creditStatus = creditStatus)
		GpaFrame(grades = grades)
		GradeSemesterListFrame(
			semesters = grades,
			onSemesterClick = onSemesterClick
		)
	}
}

@Composable
fun GradeSemesterListFrame(
	semesters: ViewResource<List<SemesterGrade>>,
	onSemesterClick: (SemesterGrade) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		// TODO polish laying out the grade grid

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				text = stringResource(id = R.string.common_term),
				modifier = Modifier.weight(1f),
				fontWeight = FontWeight.Bold
			)

			Text(
				text = stringResource(id = R.string.grades_major_gpa_short),
				modifier = Modifier.width(MajorColumnWidth),
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold
			)

			Text(
				text = stringResource(id = R.string.grades_overall_gpa_short),
				modifier = Modifier.width(OverallColumnWidth),
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold
			)
		}

		Divider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
			color = Color.Gray
		)

		when(semesters) {
			is ViewResource.Loading -> {

			}
			is ViewResource.Success -> {
				GradeSemesterList(
					semesters = semesters.value,
					onSemesterClick = onSemesterClick
				)
			}
			is ViewResource.Error -> {

			}
		}
	}
}

@Composable
fun GradeSemesterList(semesters: List<SemesterGrade>, onSemesterClick: (SemesterGrade) -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		for(semester in semesters) {
			SemesterEntry(
				semester = semester,
				onSemesterClick = onSemesterClick
			)
		}
	}
}

@Composable
fun SemesterEntry(semester: SemesterGrade, onSemesterClick: (SemesterGrade) -> Unit) {
	val majorGpa = remember(semester) { getGpa(semester.grades.filter { it.course.isMajor }) }
	val overallGpa = remember(semester) { getGpa(semester.grades) }

	Row(
		modifier = Modifier
			.clickable {
				onSemesterClick(semester)
			}
			.fillMaxWidth()
			.padding(16.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = stringResource(id = R.string.common_semester, semester.year, semester.term),
			modifier = Modifier.weight(1f)
		)

		Text(
			text = majorGpa.toString(),
			modifier = Modifier.width(MajorColumnWidth),
			textAlign = TextAlign.Center
		)

		Text(
			text = overallGpa.toString(),
			modifier = Modifier.width(OverallColumnWidth),
			textAlign = TextAlign.Center
		)
	}
}

@Composable
fun SchoolRegisterFrame(schoolRegister: ViewResource<SchoolRegister>) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 16.dp, end = 16.dp, top = 16.dp)
	) {
		if(schoolRegister is ViewResource.Success) {
			val data = schoolRegister.value

			Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				Text(
					text = stringResource(id = R.string.common_department),
					fontWeight = FontWeight.Bold
				)
				Text(text = data.department)
			}

			Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				Text(
					text = stringResource(id = R.string.common_student_id),
					fontWeight = FontWeight.Bold
				)
				Text(text = data.studentId)
			}

			Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				Text(
					text = stringResource(id = R.string.common_name),
					fontWeight = FontWeight.Bold
				)
				Text(text = data.studentName)
			}
		}
	}
}
