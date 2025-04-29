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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import java.time.ZonedDateTime
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.OnlineContentEntry
import me.onebone.openklas.klas.model.Semester
import me.onebone.openklas.klas.model.Timetable
import me.onebone.openklas.ui.shared.compose.base.KlasTheme
import me.onebone.openklas.utils.ViewResource
import me.onebone.openklas.utils.dateToShortString
import me.onebone.openklas.widget.EmptyIndicator
import me.onebone.openklas.widget.FullWidthRefreshableError

@Composable
fun HomeScreen(
	currentSemester: Semester?,
	schedule: ViewResource<List<Timetable.Entry>>,
	assignments: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Homework>>>,
	impendingAssignments: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Homework>>>,
	videos: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Video>>>,
	impendingVideos: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Video>>>,
	onOnlineContentsRefresh: () -> Unit,
	onScheduleRefresh: () -> Unit,
	now: ZonedDateTime
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Semester(semester = currentSemester)

		Schedule(
			schedule = schedule,
			onScheduleRefresh = onScheduleRefresh,
			now = now
		)

		AssignmentFrame(
			assignments = assignments,
			impending = impendingAssignments,
			onAssignmentRefresh = onOnlineContentsRefresh,
			now = now
		)

		OnlineVideoFrame(
			videos = videos,
			impending = impendingVideos,
			onOnlineVideoRefresh = onOnlineContentsRefresh,
			now = now
		)
	}
}

@Composable
fun Semester(semester: Semester?) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colors.background)
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
	items: ViewResource<List<Pair<BriefSubject, OnlineContentEntry>>>,
	impending: ViewResource<List<Pair<BriefSubject, OnlineContentEntry>>>,
	onOnlineContentsRefresh: () -> Unit,
	now: ZonedDateTime
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colors.background)
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
					if(impending !is ViewResource.Success || impending.value.isEmpty()) R.color.green
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

		if(items is ViewResource.Success && impending is ViewResource.Success) {
			if(items.value.isEmpty()) {
				EmptyIndicator(
					modifier = Modifier
						.fillMaxWidth()
						.height(80.dp),
					message = noOnlineContent
				)
			}else{
				LazyRow(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp)
				) {
					item {
						Spacer(modifier = Modifier.width(4.dp))
					}

					val isImpendingEmpty = impending.value.isEmpty()

					items(
						if(isImpendingEmpty) items.value
						else impending.value
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
		}else if(items is ViewResource.Error || impending is ViewResource.Error) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 30.dp),
				contentAlignment = Alignment.Center
			) {
				FullWidthRefreshableError(
					onRefresh = onOnlineContentsRefresh,
					message = if(items is ViewResource.Error) {
						items.error
					}else{
						// at this time, [impending] should be error
						check(impending is ViewResource.Error)
						impending.error
					}.message
				)
			}
		}else{
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
			) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center)
				)
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
				color = Color.White,
				fontSize = 12.sp
			)

			Spacer(modifier = Modifier.width(4.dp))

			Text(
				text = subject.name,
				fontWeight = Bold,
				color = Color.Black,
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}

		Text(
			text = entry.title,
			fontSize = 14.sp,
			color = Color.Black
		)
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	KlasTheme {
		Surface(
			color = colorResource(R.color.super_light_gray),
			modifier = Modifier.fillMaxSize()
		) {
			HomeScreen(
				currentSemester = Semester("2021,1", "2021년도 1학기", emptyList()),
				schedule = ViewResource.Success(listOf(
					Timetable.Entry(1, 3, "M87", "아인슈타인", 2, "일반상대성이론실험", "", "", 1),
					Timetable.Entry(1, 5, "미지정", "히키가야 하치만", 1, "5개기본호흡", "", "", 2)
				)),
				now = ZonedDateTime.of(2021, 5, 8, 16, 0, 0, 0, ZoneId.of("Asia/Seoul")),
				assignments = ViewResource.Success(listOf()),
				impendingAssignments = ViewResource.Success(listOf()),
				videos = ViewResource.Success(listOf()),
				impendingVideos = ViewResource.Success(listOf()),
				onOnlineContentsRefresh = {},
				onScheduleRefresh = {}
			)
		}
	}
}
