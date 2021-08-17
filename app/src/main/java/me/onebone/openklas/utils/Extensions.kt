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

package me.onebone.openklas.utils

import com.google.android.material.textfield.TextInputLayout
import me.onebone.openklas.klas.error.KlasNoDataError
import me.onebone.openklas.klas.error.KlasSessionInvalidError
import retrofit2.Response

fun <T> Response<T>.validateSession(): Resource<T> {
	if(raw().isRedirect) {
		return Resource.Error(KlasSessionInvalidError())
	}

	val body = body() ?: return Resource.Error(KlasNoDataError())

	return Resource.Success(body)
}

fun TextInputLayout.getSelectionText(): String = editText!!.text.toString()

fun TextInputLayout.getSelection(items: Array<String>, default: Int = 0): Int {
	val selectedText = getSelectionText()

	return when(val index = items.indexOf(selectedText)) {
		-1 -> default
		else -> index
	}
}
