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

package me.onebone.openklas.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.OnlineContentEntry
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.klas.model.Timetable
import me.onebone.openklas.utils.dateToShortString
import java.time.ZonedDateTime

@Composable
fun HomeScreen(
	currentSemester: Semester?,
	schedule: List<Timetable.Entry>?,
	assignments: List<Pair<BriefSubject, OnlineContentEntry.Homework>>?,
	impendingAssignments: List<Pair<BriefSubject, OnlineContentEntry.Homework>>?,
	videos: List<Pair<BriefSubject, OnlineContentEntry.Video>>?,
	impendingVideos: List<Pair<BriefSubject, OnlineContentEntry.Video>>?,
	now: ZonedDateTime
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Semester(semester = currentSemester)

		Schedule(schedule = schedule, now = now)

		AssignmentFrame(assignments = assignments, impending = impendingAssignments, now = now)

		OnlineVideoFrame(videos = videos, impending = impendingVideos, now = now)
	}
}

@Composable
fun Semester(semester: Semester?) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colors.surface)
			.padding(8.dp)
	) {
		if(semester == null) {
			Box(
				modifier = Modifier
					.size(120.dp, 20.dp)
					.background(colorResource(R.color.placeholder))
			)
		}else{
			Text(
				text = semester.label,
				fontWeight = Bold
			)
		}
	}
}

/**
 * Note that this composable assumes that [impending] is a subset of [items]
 */
@Composable
fun OnlineContentListFrame(
	icon: Painter,
	iconDescription: String?,
	title: String,
	noOnlineContent: String,
	items: List<Pair<BriefSubject, OnlineContentEntry>>?,
	impending: List<Pair<BriefSubject, OnlineContentEntry>>?,
	now: ZonedDateTime
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colors.surface)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.CenterStart)
			) {
				val color = colorResource(
					if(impending?.isEmpty() != false) R.color.green
					else R.color.red
				)

				Icon(
					painter = icon,
					contentDescription = iconDescription,
					modifier = Modifier.align(Alignment.CenterVertically),
					tint = color
				)

				Text(
					text = title,
					modifier = Modifier
						.align(Alignment.CenterVertically)
						.padding(horizontal = 8.dp),
					color = color,
					fontSize = 18.sp,
					fontWeight = Bold
				)
			}
		}

		if(items == null || impending == null) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
			) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center)
				)
			}
		}else{
			if(items.isEmpty()) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(80.dp)
				) {
					Text(
						text = noOnlineContent,
						modifier = Modifier.align(Alignment.Center),
						color = colorResource(R.color.gray),
						fontSize = 14.sp
					)
				}
			}else{
				LazyRow(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp)
				) {
					item {
						Spacer(modifier = Modifier.width(4.dp))
					}

					val isImpendingEmpty = impending.isEmpty()

					items(
						if(isImpendingEmpty) items
						else impending
					) {
						OnlineContentListItem(
							item = it,
							now = now,
							isImpending = !isImpendingEmpty
						)
					}

					item {
						Spacer(modifier = Modifier.width(4.dp))
					}
				}
			}
		}
	}
}

@Composable
fun OnlineContentListItem(
	item: Pair<BriefSubject, OnlineContentEntry>,
	now: ZonedDateTime,
	isImpending: Boolean
) {
	val subject = item.first
	val entry = item.second

	Column(
		modifier = Modifier
			.padding(4.dp)
			.wrapContentWidth()
			.background(
				colorResource(
					if(isImpending) R.color.light_red
					else R.color.light_green
				)
			)
			.padding(12.dp)
	) {
		Row {
			Text(
				text = dateToShortString(LocalContext.current, entry.dueDate, now),
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.background(colorResource(if(isImpending) R.color.red else R.color.green))
					.padding(4.dp),
				color = colorResource(R.color.white),
				fontSize = 12.sp
			)

			Spacer(modifier = Modifier.width(4.dp))

			Text(
				text = subject.name,
				fontWeight = Bold,
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}

		Text(
			text = entry.title,
			fontSize = 14.sp
		)
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	MaterialTheme {
		Surface(
			color = colorResource(R.color.super_light_gray),
			modifier = Modifier.fillMaxSize()
		) {
			HomeScreen(
				currentSemester = Semester("2021,1", "2021년도 1학기", emptyList()),
				schedule = listOf(
					Timetable.Entry(1, 3, "M87", "아인슈타인", 2, "일반상대성이론실험", "", "", 1),
					Timetable.Entry(1, 5, "미지정", "히키가야 하치만", 1, "5개기본호흡", "", "", 2)
				),
				now = ZonedDateTime.of(2021, 5, 8, 16, 0, 0, 0, ZoneId.of("Asia/Seoul")),
				assignments = listOf(),
				impendingAssignments = listOf(),
				videos = listOf(),
				impendingVideos = listOf()
			)
		}
	}
}
