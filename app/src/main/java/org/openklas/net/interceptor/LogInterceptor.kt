package org.openklas.net.interceptor

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

			if (Config.config.printLogRelease) {
				interceptor.level = HttpLoggingInterceptor.Level.BODY
			} else {
				interceptor.level = HttpLoggingInterceptor.Level.NONE
			}

			return interceptor.intercept(chain)
		}
	}
}
