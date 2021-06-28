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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.openklas.R
import org.openklas.klas.model.SubjectGrade
import org.openklas.klas.model.SemesterGrade
import org.openklas.ui.shared.compose.bottomShadow
import org.openklas.utils.getGpa

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GradeSemesterFrame(
	grades: SemesterGrade
) {
	var selectedSubject by remember { mutableStateOf<SubjectGrade?>(null) }

	val scope = rememberCoroutineScope()

	val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
	ModalBottomSheetLayout(
		sheetContent = {
			if(selectedSubject != null) {
				val subject = selectedSubject!!

				SubjectGradeBottomSheetContent(subject = subject)
			}else{
				// Looks like ModalBottomSheetLayout does not accept empty sheetContent
				// https://issuetracker.google.com/issues/182882364
				Box(
					modifier = Modifier
						.width(1.dp)
						.height(1.dp)
				)
			}
		},
		sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
		sheetState = sheetState
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				SemesterFrame(grades = grades)

				SemesterGpaFrame(grades = grades)

				SubjectGradeListFrame(
					grades = grades,
					onSubjectClick = {
						selectedSubject = it

						scope.launch {
							sheetState.show()
						}
					}
				)
			}
		}
	}

	if(sheetState.targetValue == ModalBottomSheetValue.Hidden && sheetState.progress.fraction == 1f) {
		selectedSubject = null
	}

	BackHandler(enabled = sheetState.currentValue != ModalBottomSheetValue.Hidden) {
		scope.launch {
			sheetState.hide()
		}
	}
}

@Composable
fun SubjectChip(name: String, color: Color) {
	Box(
		modifier = Modifier
			.background(color, shape = RoundedCornerShape(16.dp))
			.padding(horizontal = 8.dp, vertical = 2.dp)
	) {
		Text(
			text = name,
			color = Color.White
		)
	}
}

@Composable
fun SubjectGradeBottomSheetContent(
	subject: SubjectGrade
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				text = subject.subjectName,
				modifier = Modifier.padding(horizontal = 16.dp),
				fontSize = 21.sp,
				fontWeight = FontWeight.Bold
			)

			Text(
				text = subject.department,
				modifier = Modifier.padding(horizontal = 16.dp),
				color = colorResource(id = R.color.grades_department)
			)
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			if(subject.course.first() == '전') {
				SubjectChip(name = stringResource(id = R.string.common_major), color = Color.Blue)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = subject.grade.grade ?: stringResource(id = R.string.grades_grade_no_input),
				color =
					if(subject.grade.grade == null)
						colorResource(R.color.grades_no_input)
					else
						Color.Black,
				modifier = Modifier
					.weight(1f),
				fontSize = 45.sp,
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold
			)

			Column(
				modifier = Modifier
					.padding(16.dp)
					.wrapContentWidth(
						align = Alignment.End
					),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				TextRow(
					key = stringResource(id = R.string.common_academic_number),
					value = subject.academicNumber
				)

				TextRow(
					key = stringResource(id = R.string.common_certificate_type),
					value = subject.certName ?: "-"
				)
			}
		}
	}
}

@Composable
fun TextRow(
	key: String,
	value: String
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = key,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = value
		)
	}
}

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
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp
			)

			Text(
				text = grade.department,
				color = colorResource(id = R.color.grades_department),
				fontSize = 15.sp
			)
		}

		Text(
			text = grade.credits.toString(),
			modifier = Modifier
				.weight(0.2f),
			textAlign = TextAlign.Center,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = grade.grade.grade ?: stringResource(id = R.string.grades_grade_no_input),
			modifier = Modifier
				.weight(0.2f),
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
			modifier = Modifier
				.weight(0.2f),
			textAlign = TextAlign.Center,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = stringResource(id = R.string.grades_grade),
			modifier = Modifier
				.weight(0.2f),
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
