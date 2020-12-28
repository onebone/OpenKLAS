package org.openklas.base

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

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.openklas.utils.fileAsString

object Config {
	@JvmStatic
	var config = ConfigItem()

	/**
	 * load Settings from given [path]
	 * @param context Context
	 * @param path String
	 */
	fun loadConfig(context: Context, path: String) {
		val configItem: ConfigItem
		val jsonStr = context.assets.fileAsString(path)

		configItem = if (TextUtils.isEmpty(jsonStr)) {
			ConfigItem()
		} else {
			Gson().fromJson(jsonStr, ConfigItem::class.java)
		}

		config = configItem
	}

	class ConfigItem {
		/**
		 * set timeout of connections
		 * default is 10000ms (10s)
		 */
		@SerializedName("ConnectTimeout")
		val connectTimeout = 10000

		/**
		 * Set flags of display details log for networking connections.
		 * if true, it will use ihsanbal/LoggingInterceptor
		 * otherwise, it will use okhttp3/logging-interceptor
		 */
		@SerializedName("ProvideDetailLog")
		val provideDetailsLog = false

		/**
		 * Set timeout of network request.
		 * detault is 20000ms (20s)
		 */
		@SerializedName("Timeout")
		val timeout = 20000

		/**
		 * Set flags of enable RxJava-Adapter in Retrofit
		 */
		@SerializedName("DisableRxAdapter")
		val disableRxAdapter = false

		/**
		 * Set flags on enable retryOnConnectionFailure in OKHttp
		 * default is true
		 */
		@SerializedName("RetryOnConnectionFailure")
		val retryOnConnectionFailure: Boolean = true

		/**
		 * Set flags on disable LogInterceptor in OKHttp
		 * default is false
		 */
		@SerializedName("NotUseLogInterceptor")
		val notUseLogInterceptor: Boolean = false

		/**
		 * Set threshold of Binary omitted size.
		 * It may helpful for large-body size
		 *
		 * default is 10000
		 */
		@SerializedName("BinaryThresholdLogInterceptor")
		val binaryThreshold: Int = 10000
	}
}
