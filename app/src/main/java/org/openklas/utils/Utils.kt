@file:JvmName("Utils")
@file:JvmMultifileClass

package org.openklas.utils

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

import android.app.DownloadManager
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import org.openklas.R
import org.openklas.klas.KlasUri
import org.openklas.klas.model.Syllabus
import org.openklas.klas.model.SyllabusSummary
import org.openklas.ui.syllabus.page.summary.TUTOR_PROFESSOR
import org.openklas.ui.syllabus.page.summary.TUTOR_SECONDARY_PROFESSOR
import org.openklas.ui.syllabus.page.summary.TUTOR_TEACHING_ASSISTANT
import java.net.URL
import java.nio.charset.Charset

fun AssetManager.fileAsString(filename: String): String {
	return open(filename).use {
		it.readBytes().toString(Charset.defaultCharset())
	}
}

fun periodToTime(period: Int): String {
	val map = mapOf(
		0 to "8:00",
		1 to "9:00",
		2 to "10:30",
		3 to "12:00",
		4 to "13:30",
		5 to "15:00",
		6 to "16:30",
		7 to "18:00"
	)

	return map[period] ?: "N/A"
}

fun dp2px(context: Context, dp: Float) =
	TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

fun syllabusCourseToColor(context: Context, course: String?): Int {
	if(course == null) return Color.WHITE
	if(course.length != 2) return Color.WHITE

	return ResourcesCompat.getColor(context.resources, when(course) {
		"전필" -> R.color.blue
		"전선" -> R.color.green
		"교필" -> R.color.purple_500
		"교선" -> R.color.teal_700
		else -> R.color.white
	}, null)
}

fun syllabusSummaryToAcademicNumber(entry: SyllabusSummary): String {
	return "${entry.departmentCode}-${entry.targetGrade}-${entry.openGwamokNo}-${entry.division}"
}

fun syllabusToAcademicNumber(entry: Syllabus?): String {
	if(entry == null) return ""

	return "${entry.departmentCode}-${entry.targetGrade}-${entry.openGwamokNo}-${entry.division}"
}

fun tutorTypeToColor(context: Context, type: Int?): Int {
	return ResourcesCompat.getColor(context.resources, when(type) {
		TUTOR_PROFESSOR -> R.color.professor
		TUTOR_SECONDARY_PROFESSOR -> R.color.secondary_professor
		TUTOR_TEACHING_ASSISTANT -> R.color.teaching_assistant
		else -> R.color.unknown_tutor_type
	}, null)
}

fun downloadFile(context: Context, url: String, fileName: String) {
	val targetUrl = URL(URL(KlasUri.ROOT_URI), url).toString()

	val request = DownloadManager.Request(Uri.parse(targetUrl)).apply {
		setTitle(fileName)
		setDescription(context.resources.getString(R.string.download_description))
		setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
		setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
	}

	val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
	if(manager.enqueue(request) == 0L) {
		Toast.makeText(context, R.string.download_unavailable, Toast.LENGTH_LONG).show()
	}
}
