package org.openklas.net.interceptor

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

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.openklas.base.Config
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogInterceptor @Inject constructor() : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		if (Config.config.provideDetailsLog) {
			val interceptor = LoggingInterceptor.Builder()
				.loggable(true)
				.setLevel(Level.BASIC)
				.log(Platform.INFO)
				.request("Request")
				.response("Response")
				.build()

			return interceptor.intercept(chain)
		} else {
			val interceptor =
				CustomLoggingInterceptor(
					HttpLoggingInterceptor.Logger.DEFAULT,
					Config.config.binaryThreshold
				)

			interceptor.level = HttpLoggingInterceptor.Level.BODY

			return interceptor.intercept(chain)
		}
	}
}
