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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.openklas.R
import org.openklas.klas.model.SchoolRegister
import org.openklas.utils.ViewResource

@Composable
fun GradeScreen() {
	val viewModel = viewModel<GradeViewModel>()

	val grades = viewModel.grades.collectAsState(initial = ViewResource.Loading())
	val schoolRegister by viewModel.schoolRegister.collectAsState(initial = ViewResource.Loading())
	var semester by remember { mutableStateOf<String?>(null) }

	MaterialTheme {
		Surface {
			if(semester == null) {
				GradeOverviewLayout(
					schoolRegister = schoolRegister,
					onSemesterClick = {
						semester = it
					}
				)
			}
		}
	}
}

@Composable
fun GradeOverviewLayout(
	schoolRegister: ViewResource<SchoolRegister>,
	onSemesterClick: (String) -> Unit
) {
	Column {
		SchoolRegisterFrame(schoolRegister = schoolRegister)
	}
}

@Composable
fun SchoolRegisterFrame(schoolRegister: ViewResource<SchoolRegister>) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
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
