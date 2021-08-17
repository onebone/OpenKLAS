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

package me.onebone.openklas.ui.grade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.SemesterGrade
import me.onebone.openklas.klas.model.SubjectGrade
import me.onebone.openklas.ui.shared.compose.bottomShadow
import me.onebone.openklas.utils.getGpa

private val CreditColumnWidth = 50.dp
private val GradeColumnWidth = 50.dp

@Composable
fun SubjectGradeListFrame(
	grades: SemesterGrade,
	onSubjectClick: (SubjectGrade) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		// TODO polish laying out the grade grid
		SubjectGradeListHeader()

		Divider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp)
		)

		Column(
			modifier = Modifier
				.fillMaxWidth()
		) {
			for(grade in grades.grades) {
				SubjectGradeListEntry(
					grade = grade,
					onSubjectClick = onSubjectClick
				)
			}
		}
	}
}

@Composable
fun SubjectGradeListEntry(
	grade: SubjectGrade,
	onSubjectClick: (SubjectGrade) -> Unit
) {
	Row(
		modifier = Modifier
			.clickable {
				onSubjectClick(grade)
			}
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			modifier = Modifier
				.weight(1f)
		) {
			Text(
				text = grade.subjectName,
				fontWeight = FontWeight.Bold
			)

			Text(
				text = grade.department,
				color = colorResource(id = R.color.grades_department)
			)
		}

		Text(
			text = grade.credits.toString(),
			modifier = Modifier.width(CreditColumnWidth),
			textAlign = TextAlign.Center,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = grade.grade.grade ?: stringResource(id = R.string.grades_grade_no_input),
			modifier = Modifier.width(GradeColumnWidth),
			textAlign = TextAlign.Center,
			color =
			if(grade.grade.grade == null) colorResource(id = R.color.grades_no_input)
			else Color.Unspecified,
			fontWeight = FontWeight.Bold
		)
	}
}

@Composable
fun SubjectGradeListHeader() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Text(
			text = stringResource(id = R.string.common_subject),
			modifier = Modifier
				.weight(1f),
			fontWeight = FontWeight.Bold
		)

		Text(
			text = stringResource(id = R.string.common_credits),
			modifier = Modifier.width(CreditColumnWidth),
			textAlign = TextAlign.Center,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = stringResource(id = R.string.grades_grade),
			modifier = Modifier.width(GradeColumnWidth),
			textAlign = TextAlign.Center,
			fontWeight = FontWeight.Bold
		)
	}
}

@Composable
fun SemesterGpaFrame(grades: SemesterGrade) {
	val overallSubjects = remember(grades) {
		grades.grades.filter {
			it.grade.isGpaCounted
		}
	}

	val majorSubjects = remember(overallSubjects) {
		overallSubjects.filter {
			it.course.isMajor
		}
	}

	val majorGpa = remember(grades) { getGpa(majorSubjects) }
	val overallGpa = remember(grades) { getGpa(overallSubjects) }

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		GpaEntry(
			modifier = Modifier
				.weight(1f)
				.aspectRatio(1f),
			text = stringResource(id = R.string.grades_major_gpa),
			gpa = majorGpa,
			gradesGroup = remember(majorSubjects) { majorSubjects.groupBy { it.grade.grade?.first() ?: ' ' } }
		)

		GpaEntry(
			modifier = Modifier
				.weight(1f)
				.aspectRatio(1f),
			text = stringResource(id = R.string.grades_overall_gpa),
			gpa = overallGpa,
			gradesGroup = remember(overallSubjects) { overallSubjects.groupBy { it.grade.grade?.first() ?: ' ' } }
		)
	}
}

@Composable
fun SemesterFrame(grades: SemesterGrade) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.bottomShadow(
				4.dp,
				colorResource(R.color.shadow_start),
				colorResource(R.color.shadow_end)
			)
			.padding(16.dp)
	) {
		Text(
			text = stringResource(id = R.string.common_semester, grades.year, grades.term),
			fontWeight = FontWeight.Bold,
			fontSize = 18.sp
		)
	}
}
