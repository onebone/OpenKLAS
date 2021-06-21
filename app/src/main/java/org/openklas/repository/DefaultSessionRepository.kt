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

package org.openklas.repository

import org.openklas.data.AccountDataSource
import org.openklas.data.SessionDataSource
import org.openklas.utils.Resource
import javax.inject.Inject

class DefaultSessionRepository @Inject constructor(
	private val sessionDataSource: SessionDataSource,
	private val accountDataSource: AccountDataSource
): SessionRepository {
	override suspend fun testSession(): Boolean {
		return sessionDataSource.testSession()
	}

	override suspend fun tryLogin(): Boolean {
		val account = accountDataSource.getAccount() ?: return false

		return sessionDataSource.tryLogin(account.username, account.password) is Resource.Success
	}

	override fun getSavedCredential(): Credential? {
		val account = accountDataSource.getAccount() ?: return null

		return Credential(account.username, account.password)
	}
}
