package org.openklas.base.impl.port

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
import android.content.Intent
import android.os.Bundle
import pyxis.uzuki.live.richutilskt.module.reference.ActivityReference


internal object ActivityInterfacePort {

	@JvmStatic
	fun startActivity(cls: Class<*>, flags: Int, extras: Bundle?) {
		val context = ActivityReference.getContext()
		val intent = Intent(context, cls)

		if (context !is Activity) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		}

		if (extras != null) {
			intent.putExtras(extras)
		}

		if (flags != 0) {
			intent.flags = flags
		}

		context.startActivity(intent)
	}

	@JvmStatic
	@JvmOverloads
	fun finishActivity(activity: Activity, enterAnim: Int = -1, exitAnim: Int = -1) {
		activity.finish()
		if (enterAnim != -1 && exitAnim != -1) {
			activity.overridePendingTransition(enterAnim, exitAnim)
		}
	}

	@JvmStatic
	@JvmOverloads
	fun finishActivity(cls: Class<*>, enterAnim: Int = -1, exitAnim: Int = -1) {
		val activities = ActivityReference.getActivityList()

		for (activity in activities) {
			if (activity.javaClass == cls) {
				finishActivity(
					activity,
					enterAnim,
					exitAnim
				)
				break
			}
		}
	}

	@JvmStatic
	@JvmOverloads
	fun finishAllActivities(enterAnim: Int = -1, exitAnim: Int = -1) {
		val activities = ActivityReference.getActivityList()

		for (i in activities.indices.reversed()) {
			val activity = activities[i]
			finishActivity(
				activity,
				enterAnim,
				exitAnim
			)
		}
	}
}
