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

import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.CreditStatus
import me.onebone.openklas.utils.ViewResource


@Composable
fun CreditStatusFrame(creditStatus: ViewResource<CreditStatus>) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Text(
			text = stringResource(id = R.string.grades_credit_status),
			fontWeight = FontWeight.Bold,
			fontSize = 18.sp
		)

		CreditStatusBody(creditStatus = creditStatus)
	}
}

@Composable
fun CreditStatusBody(creditStatus: ViewResource<CreditStatus>) {
	when(creditStatus) {
		is ViewResource.Success -> {
			val data = creditStatus.value

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					Spacer(modifier = Modifier.weight(2f))

					Text(
						text = stringResource(id = R.string.common_major),
						textAlign = TextAlign.End,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.weight(1f)
					)

					Text(
						text = stringResource(id = R.string.common_elective),
						textAlign = TextAlign.End,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.weight(1f)
					)

					Text(
						text = stringResource(id = R.string.common_others),
						textAlign = TextAlign.End,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.weight(1f)
					)
				}

				CreditStatusRow(
					tag = stringResource(id = R.string.grades_applied),
					operator = "+",
					color = colorResource(id = R.color.grades_applied),
					credits = data.applied
				)

				CreditStatusRow(
					tag = stringResource(id = R.string.grades_deleted),
					operator = "-",
					color = colorResource(id = R.color.grades_deleted),
					credits = data.deleted
				)

				Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black)

				CreditStatusRow(
					tag = stringResource(id = R.string.grades_acquired),
					operator = "=",
					color = colorResource(id = R.color.grades_acquired),
					credits = data.acquired
				)
			}
		}
		is ViewResource.Loading -> {

		}
		is ViewResource.Error -> {

		}
	}
}

@Composable
fun CreditStatusRow(
	tag: String,
	operator: String,
	@ColorInt color: Color,
	credits: CreditStatus.Credits
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Text(
			text = tag,
			modifier = Modifier.weight(1f),
			textAlign = TextAlign.End,
			fontWeight = FontWeight.Bold,
			color = color
		)

		Text(
			text = operator,
			modifier = Modifier.weight(1f),
			textAlign = TextAlign.End,
			color = color
		)

		Text(
			text = credits.major.toString(),
			modifier = Modifier.weight(1f),
			textAlign = TextAlign.End,
			color = color
		)

		Text(
			text = credits.culture.toString(),
			modifier = Modifier.weight(1f),
			textAlign = TextAlign.End,
			color = color
		)

		Text(
			text = credits.others.toString(),
			modifier = Modifier.weight(1f),
			textAlign = TextAlign.End,
			color = color
		)
	}
}
