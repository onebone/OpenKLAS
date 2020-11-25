package org.openklas.net.interceptor

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

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class CustomLoggingInterceptor(
	private val logger: HttpLoggingInterceptor.Logger,
	private val thresholdBinary: Int
) : Interceptor {
	@Volatile
	var level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val level = this.level

		val request = chain.request()
		if (level == HttpLoggingInterceptor.Level.NONE) return chain.proceed(request)

		val logBody = level == HttpLoggingInterceptor.Level.BODY
		val logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS

		val requestBody = request.body()
		val hasRequestBody = requestBody != null

		val connection = chain.connection()
		val requestStartMessage = "--> ${request.method()} ${request.url()} " +
				"${if (connection != null) " " + connection.protocol() else ""} " +
				if (!logHeaders) " (" + requestBody?.contentLength() + "$BYTE_BODY)" else ""

		logger.log(requestStartMessage)

		if (logHeaders) {
			requestBody?.contentType()?.let { logger.log("$CONTENT_TYPE: $it") }
			requestBody?.contentLength()?.let { logger.log("$CONTENT_LENGTH: $it") }

			val headers = request.headers()
			for (i in 0 until headers.size()) {
				val name = headers.name(i)
				if (!CONTENT_TYPE.equals(name, ignoreCase = true) && !CONTENT_LENGTH.equals(
						name,
						ignoreCase = true
					)
				) {
					logger.log("$name: ${headers.value(i)}")
				}
			}

			if (!logBody || !hasRequestBody) {
				logger.log("$REQUEST_END ${request.method()}")
			} else if (bodyEncoded(request.headers())) {
				logger.log("$REQUEST_END ${request.method()} $ENCODE_BODY")
			} else {
				val buffer = Buffer()
				requestBody?.writeTo(buffer)

				var charset: Charset? =
					UTF8
				val contentType = requestBody?.contentType()
				if (contentType != null) {
					charset = contentType.charset(UTF8)
				}

				logger.log("")
				if (isPlaintext(
						buffer
					) && requestBody?.contentLength() ?: 0 < thresholdBinary
				) {
					logger.log(buffer.readString(charset ?: UTF8))
					logger.log("$REQUEST_END ${request.method()} (${requestBody?.contentLength()}$BYTE_BODY)")
				} else {
					logger.log("$REQUEST_END ${request.method()} (binary ${requestBody?.contentLength()}$BYTE_BODY omitted)")
				}
			}
		}

		val startNs = System.nanoTime()
		val response: Response
		try {
			response = chain.proceed(request)
		} catch (e: Exception) {
			logger.log("$RESPONSE_END FAILED: $e")
			throw e
		}

		val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
		val responseBody = response.body()
		val contentLength = responseBody?.contentLength()
		val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
		logger.log(
			"<-- ${response.code()} ${if (response.message()
					.isEmpty()
			) "" else ' ' + response.message()}" +
					" ${response.request()
						.url()} (${tookMs}ms ${if (!logHeaders) ", $bodySize body" else ""})"
		)

		if (!logHeaders) return response

		val headers = response.headers()
		for (i in 0 until headers.size()) {
			logger.log(headers.name(i) + ": " + headers.value(i))
		}

		if (!logBody || !HttpHeaders.hasBody(response)) {
			logger.log(RESPONSE_END)
		} else if (bodyEncoded(response.headers())) {
			logger.log("$RESPONSE_END $ENCODE_BODY")
		} else {
			val source = responseBody?.source()
			source?.request(java.lang.Long.MAX_VALUE)
			val buffer = source?.buffer() ?: Buffer()

			var charset: Charset? =
				UTF8
			val contentType = responseBody?.contentType()
			if (contentType != null) charset = contentType.charset(UTF8)

			if (!isPlaintext(
					buffer
				) || requestBody?.contentLength() ?: 0 > thresholdBinary
			) {
				logger.log("")
				logger.log("$RESPONSE_END (binary ${buffer.size()} $BYTE_BODY omitted")
				return response
			}

			if (contentLength != 0L) {
				logger.log("")
				logger.log(buffer.clone().readString(charset ?: UTF8))
			}

			logger.log("$RESPONSE_END (${buffer.size()}$BYTE_BODY)")
		}

		return response
	}

	private fun bodyEncoded(headers: Headers): Boolean {
		val contentEncoding = headers.get("Content-Encoding")
		return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
	}

	companion object {
		private val UTF8 = Charset.forName("UTF-8")
		const val CONTENT_TYPE = "Content-Type"
		const val CONTENT_LENGTH = "Content-Length"
		const val REQUEST_END = "--> END"
		const val RESPONSE_END = "<-- END HTTP"
		const val ENCODE_BODY = "(encoded body omitted)"
		const val BYTE_BODY = "-byte body"

		internal fun isPlaintext(buffer: Buffer): Boolean {
			try {
				val prefix = Buffer()
				val byteCount = if (buffer.size() < 64) buffer.size() else 64
				buffer.copyTo(prefix, 0, byteCount)
				for (i in 0..15) {
					if (prefix.exhausted()) {
						break
					}
					val codePoint = prefix.readUtf8CodePoint()
					if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
						return false
					}
				}
				return true
			} catch (e: EOFException) {
				return false // Truncated UTF-8 sequence.
			}

		}
	}
}
