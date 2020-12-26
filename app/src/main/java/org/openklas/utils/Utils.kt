@file:JvmName("Utils")
@file:JvmMultifileClass

package org.openklas.utils

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
import android.content.res.AssetManager
import android.view.View
import android.view.ViewGroup
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

fun AssetManager.fileAsString(filename: String): String {
	return open(filename).use {
		it.readBytes().toString(Charset.defaultCharset())
	}
}
