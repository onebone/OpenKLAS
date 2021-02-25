package org.openklas.base

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

import androidx.lifecycle.LiveData
import org.openklas.utils.Event
import org.openklas.utils.Result

interface SessionViewModelDelegate {
	val mustAuthenticate: LiveData<Event<Unit>>

	/**
	 * Performs an request that requires a session. When the request has once failed
	 * due to session invalidation, it will try to login with a saved credentials and
	 * try requesting again.
	 *
	 * @param f Request procedure.
	 *
	 * @return The result of the request.
	 */
	suspend fun <T> requestWithSession(f: suspend () -> Result<T>): Result<T>
}
