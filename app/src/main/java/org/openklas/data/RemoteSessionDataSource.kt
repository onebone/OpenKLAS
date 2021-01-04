package org.openklas.data

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

import io.reactivex.Single
import org.openklas.klas.KlasClient
import javax.inject.Inject

class RemoteSessionDataSource @Inject constructor(
	private val klas: KlasClient
): SessionDataSource {
	override fun tryLogin(username: String, password: String): Single<Boolean> {
		return klas.login(username, password)
			.flatMap {
				Single.just(true)
			}
			.onErrorResumeNext(Single.just(false))
	}
}
