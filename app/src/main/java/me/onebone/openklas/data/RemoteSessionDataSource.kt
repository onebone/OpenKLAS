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

package me.onebone.openklas.data

import me.onebone.openklas.klas.KlasClient
import me.onebone.openklas.utils.Resource
import javax.inject.Inject

class RemoteSessionDataSource @Inject constructor(
	private val klas: KlasClient
): SessionDataSource {
	override suspend fun testSession(): Boolean {
		return klas.testSession()
	}

	override suspend fun tryLogin(username: String, password: String): Resource<Unit> {
		return when(val result = klas.login(username, password)) {
			is Resource.Success -> Resource.Success(Unit)
			is Resource.Error -> Resource.Error(result.error)
		}
	}
}
