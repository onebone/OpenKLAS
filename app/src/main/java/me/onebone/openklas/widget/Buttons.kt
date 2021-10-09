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

package me.onebone.openklas.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.onebone.openklas.R

@Composable
fun RefreshButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	Row(
		modifier = modifier
			.clickable { onClick() }
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			modifier = Modifier
				.padding(4.dp),
			painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
			contentDescription = null,
			tint = MaterialTheme.colors.primary
		)

		Text(
			text = stringResource(R.string.common_refresh),
			color = MaterialTheme.colors.primary
		)
	}
}
