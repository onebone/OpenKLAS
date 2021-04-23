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

package org.openklas.utils

import android.graphics.Color
import androidx.annotation.ColorInt

object HtmlUtils {
	/**
	 * Naive CSS entry extractor
	 */
	fun extractCssEntry(css: String, key: String): String? {
		val pattern = Regex("(?:;|^)\\s*$key\\s*:\\s*(.*?);", RegexOption.MULTILINE)
		val result = pattern.find(css) ?: return null

		return result.groupValues[1]
	}

	@ColorInt
	fun parseColor(str: String?): Int? {
		if(str == null) return null

		return runCatching {
			Color.parseColor(str)
		}.getOrElse {
			val pattern =
				Regex("^\\s*rgb\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)\\s*\$")
			val result = pattern.find(str) ?: return null

			val values = result.groupValues
			val r = values[1].toLong()
			val g = values[2].toLong()
			val b = values[3].toLong()

			return (0xff000000 or (r shl 16) or (g shl 8) or b).toInt()
		}
	}
}
