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

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.bumptech.glide.BuildConfig
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser
import pyxis.uzuki.live.nyancat.NyanCatGlobal
import pyxis.uzuki.live.nyancat.config.LoggerConfig
import pyxis.uzuki.live.nyancat.config.TriggerTiming
import pyxis.uzuki.live.richutilskt.module.reference.ActivityReference

/**
 * OpenKlas
 * Class: BaseApplication
 * Created by limmoong on 2020/11/10.
 *
 * Description:
 */
@AttributeParser("org.openklas")
abstract class BaseApplication : Application() {
	abstract val configFilePath: String

	override fun onCreate() {
		super.onCreate()
		Config.loadConfig(this, configFilePath)

		val config = LoggerConfig(
			packageName, BuildConfig.DEBUG,
			if (Config.config.printLogRelease) TriggerTiming.ALL else TriggerTiming.ONLY_DEBUG
		)
		NyanCatGlobal.breed(config)

		ActivityReference.initialize(this)
	}

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}
}
