@file:JvmName("Utils")
@file:JvmMultifileClass

package org.openklas.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.res.AssetManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import pyxis.uzuki.live.richutilskt.utils.hideKeyboard
import java.nio.charset.Charset

/**
 * 에디트 박스 이외 영역 터치시 키보드 숨김 메서드
 */
fun setupEditContentScrollable(view: View, activity: Activity) {
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            activity.hideKeyboard()
            false
        }
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            setupEditContentScrollable(
                view.getChildAt(i),
                activity
            )
        }
    }
}

@Suppress("DEPRECATION")
fun Activity.setTurnScreenOnLock() {
    val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager?
    when {
        android.os.Build.VERSION.SDK_INT >= 27 -> {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            keyguardManager?.requestDismissKeyguard(this, null)
        }
        android.os.Build.VERSION.SDK_INT == 26 -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            keyguardManager?.requestDismissKeyguard(this, null)
        }
        else -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }
    }

    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun AssetManager.fileAsString(filename: String): String {
    return open(filename).use {
        it.readBytes().toString(Charset.defaultCharset())
    }
}