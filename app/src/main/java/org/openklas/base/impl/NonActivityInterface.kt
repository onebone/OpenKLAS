package org.openklas.base.impl

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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pyxis.uzuki.live.richutilskt.module.reference.ActivityReference

interface NonActivityInterface {

	@JvmDefault
	fun startActivity(intent: Intent) {
		val context = ActivityReference.getContext()

		if (context !is Activity) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		}

		context.startActivity(intent)
	}

	/**
	 * 결과 설정
	 *
	 * @param resultCode
	 * @param bundle
	 */
	@JvmDefault
	fun setResult(resultCode: Int, bundle: Bundle? = null) {
		val activity = requireActivity()
		if (activity.isDestroyed) return

		val intent = Intent()
		if (bundle != null) intent.putExtras(bundle)

		activity.setResult(resultCode, intent)
	}

	@JvmDefault
	fun requireActivity(): Activity {
		return ActivityReference.getActivtyReference()
			?: throw IllegalStateException("Activity not fetched from cache")
	}

	@JvmDefault
	fun requireContext(): Context {
		return ActivityReference.getContext()
	}

	@JvmDefault
	fun requireAppCompatActivity(): AppCompatActivity {
		val activity = requireActivity()
		return activity as? AppCompatActivity
			?: throw IllegalStateException("Activity not inherited by AppCompatActivity")
	}
}
