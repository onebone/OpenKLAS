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

@file:JvmName("Utils")
@file:JvmMultifileClass

package me.onebone.openklas.utils

import android.app.DownloadManager
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import me.onebone.openklas.R
import me.onebone.openklas.klas.KlasUri
import me.onebone.openklas.klas.model.SubjectGrade
import me.onebone.openklas.klas.model.Syllabus
import me.onebone.openklas.klas.model.SyllabusSummary
import me.onebone.openklas.ui.syllabus.page.summary.TUTOR_PROFESSOR
import me.onebone.openklas.ui.syllabus.page.summary.TUTOR_SECONDARY_PROFESSOR
import me.onebone.openklas.ui.syllabus.page.summary.TUTOR_TEACHING_ASSISTANT
import java.net.URL
import java.nio.charset.Charset
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue
import kotlin.math.floor

fun AssetManager.fileAsString(filename: String): String {
	return open(filename).use {
		it.readBytes().toString(Charset.defaultCharset())
	}
}

class Time(
	val hour: Int,
	val minute: Int
) {
	init {
		require(hour in 0..23)
		require(minute in 0..59)
	}

	override fun toString(): String {
		return "%02d:%02d".format(hour, minute)
	}
}

private val PERIOD_MAP = mapOf(
	0 to Time(8, 0),
	1 to Time(9, 0),
	2 to Time(10, 30),
	3 to Time(12, 0),
	4 to Time(13, 30),
	5 to Time(15, 0),
	6 to Time(16, 30),
	7 to Time(18, 0)
)

fun periodToTime(period: Int): Time? = PERIOD_MAP[period]

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

fun dateToShortString(context: Context, target: ZonedDateTime, now: ZonedDateTime): String {
	val duration = Duration.between(now, target)

	val days = duration.toDays().absoluteValue
	val hours = duration.toHours().absoluteValue
	val minutes = duration.toMinutes().absoluteValue

	val isBefore = duration.isNegative

	return if(days > 30) {
		val formatter = DateTimeFormatter.ofPattern(context.resources.getString(R.string.common_date_format_short))
		target.format(formatter)
	}else if(days > 0) {
		context.resources.getQuantityString(
			if(isBefore) R.plurals.common_time_day_ago
			else R.plurals.common_left_time_day,
			days.toInt(), days
		)
	}else if(hours > 0) {
		context.resources.getQuantityString(
			if(isBefore) R.plurals.common_time_hours_ago
			else R.plurals.common_left_time_hour,
			hours.toInt(), hours
		)
	}else{
		if(minutes < 10) {
			context.resources.getString(
				if(isBefore) R.string.common_time_moment_ago
				else R.string.common_left_time_soon
			)
		}else{
			context.resources.getQuantityString(
				if(isBefore) R.plurals.common_time_minutes_ago
				else R.plurals.common_left_time_minute,
				minutes.toInt(), minutes
			)
		}
	}
}

fun getGpa(grades: List<SubjectGrade>): Float {
	val gpaSubjects = grades.filter {
		it.grade.isGpaCounted
	}

	val credits = gpaSubjects.sumOf { it.credits }

	return if(credits > 0) {
		(floor(gpaSubjects.sumOf {
			it.credits * it.grade.point!!
		} * 100 / credits) / 100).toFloat()
	}else{
		0f
	}
}
