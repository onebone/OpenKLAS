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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import me.onebone.openklas.R

@Composable
fun EmptyIndicator(
	modifier: Modifier = Modifier,
	painter: Painter? = null,
	message: String
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if(painter != null) {
				Icon(
					painter = painter,
					contentDescription = null,
					tint = colorResource(R.color.gray)
				)

				Spacer(modifier = Modifier.height(8.dp))
			}

			Text(
				text = message,
				color = colorResource(R.color.gray)
			)
		}
	}
}
