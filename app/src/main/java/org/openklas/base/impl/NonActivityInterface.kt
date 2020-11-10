package org.openklas.base.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.openklas.base.ActivityReference

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