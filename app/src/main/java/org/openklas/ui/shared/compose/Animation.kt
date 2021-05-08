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

package org.openklas.ui.shared.compose

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlin.math.round

@Composable
fun blinkTransition(
	fromAlpha: Float = 0f,
	toAlpha: Float = 1f,
	durationMillis: Int = 500
): State<Float> {
	val transition = rememberInfiniteTransition()

	return transition.animateFloat(
		initialValue = fromAlpha,
		targetValue = toAlpha,
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis, easing = { value -> round(value) }),
			repeatMode = RepeatMode.Reverse
		)
	)
}
