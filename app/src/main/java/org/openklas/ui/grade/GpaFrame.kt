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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.openklas.R
import org.openklas.klas.model.SemesterGrade
import org.openklas.klas.model.SubjectGrade
import org.openklas.ui.shared.compose.PieChart
import org.openklas.ui.shared.compose.PieChartEntry
import org.openklas.utils.ViewResource
import org.openklas.utils.getGpa

@Composable
fun GpaFrame(grades: ViewResource<List<SemesterGrade>>) {
	when(grades) {
		is ViewResource.Success -> {
			val flattenGrades = remember(grades) {
				grades.value.filter {
					it.term == 1 || it.term == 2
				}.flatMap {
					it.grades
				}.filter {
					it.grade.isGpaCounted
				}
			}

			val majorGrades = remember(flattenGrades) {
				flattenGrades.filter { it.course.isMajor }
			}

			val majorGpa = remember(majorGrades) { getGpa(majorGrades) }
			val overallGpa = remember(flattenGrades) { getGpa(flattenGrades) }

			val majorGradesGroup = remember(majorGrades) { majorGrades.groupBy { it.grade.grade?.first() ?: ' ' } }
			val overallGradesGroup = remember(flattenGrades) { flattenGrades.groupBy { it.grade.grade?.first() ?: ' ' } }

			Row(
				modifier = Modifier.padding(16.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				GpaEntry(
					text = stringResource(id = R.string.grades_major_gpa),
					modifier = Modifier
						.weight(1f)
						.aspectRatio(1f),
					gpa = majorGpa,
					gradesGroup = majorGradesGroup
				)

				GpaEntry(
					text = stringResource(id = R.string.grades_overall_gpa),
					modifier = Modifier
						.weight(1f)
						.aspectRatio(1f),
					gpa = overallGpa,
					gradesGroup = overallGradesGroup
				)
			}
		}
		else -> {

		}
	}
}

@Composable
fun GpaEntry(
	modifier: Modifier,
	text: String,
	gpa: Float,
	gradesGroup: Map<Char, List<SubjectGrade>>
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = text,
			fontWeight = FontWeight.Bold
		)

		PieChart(
			text = gpa.toString(),
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(),
			entries = gradesGroup
				.mapPieChartEntry()
		)
	}
}

@Composable
private fun Map<Char, List<SubjectGrade>>.mapPieChartEntry(): List<PieChartEntry> =
	map {
		PieChartEntry(it.key.toString(), it.value.count().toDouble(), colorResource(when(it.key) {
			'A' -> R.color.grades_a
			'B' -> R.color.grades_b
			'C' -> R.color.grades_c
			'D' -> R.color.grades_d
			'F' -> R.color.grades_f
			else -> R.color.black
		})
		)
	}
