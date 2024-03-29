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

sealed class ViewResource<out T> {
	data class Success<T>(val value: T): ViewResource<T>()

	data class Error<T>(val error: Throwable): ViewResource<T>()

	class Loading<T>: ViewResource<T>()
}

fun<T> Resource<T>?.transform() = when(this) {
	is Resource.Success -> ViewResource.Success(value)
	is Resource.Error -> ViewResource.Error(error)
	else -> ViewResource.Loading()
}
