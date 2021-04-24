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

package org.openklas.data

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceCredentialDataSource @Inject constructor(
	@ApplicationContext context: Context
): CredentialDataSource {
	private val preference = EncryptedSharedPreferences.create(
		context, "org.openklas.data.PreferenceCredentialDataSource",
		MasterKey.Builder(context)
			.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
			.build(),
		EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
		EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
	)

	override var userID: String?
		get() = preference.getString(USER_ID_KEY, null)
		set(value) {
			synchronized(preference) {
				preference.edit {
					putString(USER_ID_KEY, value)
				}
			}
		}

	override var password: String?
		get() = preference.getString(PASSWORD_KEY, null)
		set(value) {
			synchronized(preference) {
				preference.edit {
					putString(PASSWORD_KEY, value)
				}
			}
		}

	private companion object {
		const val USER_ID_KEY = "USER_ID"
		const val PASSWORD_KEY = "PASSWORD"
	}
}
