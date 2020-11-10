package org.openklas.base.impl.port

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.openklas.base.ActivityReference


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