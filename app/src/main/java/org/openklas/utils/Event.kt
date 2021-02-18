package org.openklas.utils

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

import androidx.lifecycle.Observer

class Event<T: Any>(
	private val value: T
) {
	private var isHandled = false

	fun getIfNotHandled(): T? {
		if(!isHandled) {
			isHandled = true
			return value
		}

		return null
	}
}

class EventObserver<T: Any>(
	private val onChangedListener: (T) -> Unit
): Observer<Event<T>> {
	override fun onChanged(t: Event<T>?) {
		t?.getIfNotHandled()?.let {
			onChangedListener(it)
		}
	}
}

