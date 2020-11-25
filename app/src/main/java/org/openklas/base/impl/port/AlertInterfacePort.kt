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

import android.app.ProgressDialog
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.openklas.base.impl.NonActivityInterface
import pyxis.uzuki.live.richutilskt.impl.F1
import pyxis.uzuki.live.richutilskt.utils.selector

object AlertInterfacePort : NonActivityInterface {

	@JvmStatic
	fun showToast(message: String) {
		val activity = requireActivity()
		if (message.isEmpty()) return

		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
	}

	@JvmStatic
	fun showAlertDialog(
		title: String,
		message: String,
		positiveListener: DialogInterface.OnClickListener?
	) {
		val activity = requireActivity()
		if (message.isEmpty()) return

		val builder = AlertDialog.Builder(activity)
		if (title.isNotEmpty()) builder.setTitle(title)

		builder.setMessage(message)
			.setCancelable(false)
			.setPositiveButton(android.R.string.ok, positiveListener)
			.show()
	}

	@JvmStatic
	fun showSelectorDialog(items: List<String>, callback: F1<Int>) {
		val activity = requireActivity()

		activity.selector(items, { _, _, position: Int ->
			callback.invoke(position)
		})
	}

	@JvmStatic
	fun showConfirmDialog(
		title: String, message: String, positiveListener: DialogInterface.OnClickListener?,
		negativeListener: DialogInterface.OnClickListener?
	) {
		val activity = requireActivity()
		if (message.isEmpty()) return

		val builder = AlertDialog.Builder(activity)
		if (title.isNotEmpty()) builder.setTitle(title)

		builder.setMessage(message)
		builder.setCancelable(false)
		builder.setPositiveButton(android.R.string.ok, positiveListener)
			.setNegativeButton(android.R.string.no, negativeListener)
			.show()
	}

	@JvmStatic
	fun showProgressDialog(title: String, message: String): ProgressDialog? {
		val activity = requireActivity()
		if (message.isEmpty()) return null

		return ProgressDialog(activity).apply {
			setProgressStyle(ProgressDialog.STYLE_SPINNER)
			setMessage(message)
			if (title.isEmpty().not())
				setTitle(title)
			show()
		}
	}
}
