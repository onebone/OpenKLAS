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
		 * Set timeout of network request.
		 * detault is 20000ms (20s)
		 */
		@SerializedName("Timeout")
		val timeout = 20000

		/**
		 * Set flags on enable retryOnConnectionFailure in OKHttp
		 * default is true
		 */

package org.openklas.base
		@SerializedName("RetryOnConnectionFailure")
		val retryOnConnectionFailure: Boolean = true
	}
}
