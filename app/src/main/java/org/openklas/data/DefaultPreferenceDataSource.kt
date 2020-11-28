package org.openklas.data

/*
* OpenKLAS
* Copyright (C) 2020 OpenKLAS Team
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

import org.openklas.MainApplication
import pyxis.uzuki.live.richutilskt.utils.RPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPreferenceDataSource @Inject constructor(
	application: MainApplication
): PreferenceDataSource {
	private val mPreference: RPreference = RPreference.getInstance(application)

	override var userID: String?
		get() = mPreference.getString(USERID_KEY, "")
		set(userID) {
			mPreference.put(USERID_KEY, userID!!)
		}

	override var password: String?
		get() = mPreference.getString(PASSWORD_KEY, "")
		set(password) {
			mPreference.put(PASSWORD_KEY, password!!)
		}

	companion object {
		private const val USERID_KEY = "1af49084-8820-449e-9039-d86863184592"
		private const val PASSWORD_KEY = "1af49084-8820-449e-9039-d86863184593"
	}
}
