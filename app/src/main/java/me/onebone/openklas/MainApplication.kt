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

package me.onebone.openklas

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import me.onebone.openklas.base.Config

@HiltAndroidApp
class MainApplication: Application() {
	override fun onCreate() {
		super.onCreate()

		if(BuildConfig.DEBUG) {
			Firebase.analytics.setAnalyticsCollectionEnabled(false)
			Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)
		}

		Config.loadConfig(this, "config.json")
	}

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}
}
