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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.openklas.R
import org.openklas.klas.model.SemesterGrade
import org.openklas.ui.shared.compose.bottomShadow
import org.openklas.utils.getGpa

@Composable
fun GradeSemesterFrame(
	grades: SemesterGrade
) {
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		SemesterFrame(grades = grades)

		SemesterGpaFrame(grades = grades)
	}
}

@Composable
fun SemesterGpaFrame(grades: SemesterGrade) {
	val overallSubjects = remember(grades) {
		grades.grades.filter {
			it.grade.length >= 2 && !it.grade.startsWith("P") && !it.grade.startsWith("NP")
		}
	}

	val majorSubjects = remember(overallSubjects) {
		overallSubjects.filter {
			it.course.first() == '전'
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
			modifier = Modifier.weight(1f).aspectRatio(1f),
			text = stringResource(id = R.string.grades_major_gpa),
			gpa = majorGpa,
			gradesGroup = remember(majorSubjects) { majorSubjects.groupBy { it.grade.first() } }
		)

		GpaEntry(
			modifier = Modifier.weight(1f).aspectRatio(1f),
			text = stringResource(id = R.string.grades_overall_gpa),
			gpa = overallGpa,
			gradesGroup = remember(overallSubjects) { overallSubjects.groupBy { it.grade.first() } }
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
			.padding(horizontal = 16.dp, vertical = 8.dp)
	) {
		Text(
			text = stringResource(id = R.string.common_semester, grades.year, grades.term),
			fontWeight = FontWeight.Bold,
			fontSize = 18.sp
		)
	}
}
