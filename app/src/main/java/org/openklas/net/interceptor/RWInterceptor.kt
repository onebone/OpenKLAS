package org.openklas.net.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import java.io.IOException
import java.nio.charset.Charset

/**
 * OpenKlas
 * Class: RWInterceptor
 * Created by limmoong on 2020/11/10.
 *
 * Description:
 */
class RWInterceptor : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response? {
		val original = chain.request()
		val originalHttpUrl = original.url()
		val originalMethod = original.method()

		val cmd = originalHttpUrl.queryParameter("cmd")
		val param = "&cmd=$cmd"

		val requestBuilder = original.newBuilder()
			.url(originalHttpUrl.newBuilder().build())

		if (original.body() != null) {
			val requestBody = original.body()!!

			val newRequestBody = object : RequestBody() {
				override fun contentLength(): Long = requestBody.contentLength() + param.length
				override fun contentType(): MediaType? = requestBody.contentType()

				@Throws(IOException::class)
				override fun writeTo(sink: BufferedSink) {
					requestBody.writeTo(sink)
					sink.writeString(param, Charset.forName("UTF-8"))
				}
			}
			requestBuilder.method(originalMethod, newRequestBody)
		}

		return chain.proceed(requestBuilder.build())
	}
}
