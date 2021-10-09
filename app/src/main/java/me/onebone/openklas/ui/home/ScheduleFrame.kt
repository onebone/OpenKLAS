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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.Timetable
import me.onebone.openklas.ui.shared.compose.blinkTransition
import me.onebone.openklas.utils.periodToTime
import java.time.ZonedDateTime
import me.onebone.openklas.utils.ViewResource
import me.onebone.openklas.widget.RefreshButton

@Composable
fun Schedule(schedule: ViewResource<List<Timetable.Entry>>, now: ZonedDateTime) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colors.surface)
			.padding(8.dp)
	) {
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_sharp_schedule_24),
				modifier = Modifier.align(Alignment.CenterVertically),
				tint = colorResource(R.color.green),
				contentDescription = stringResource(R.string.a11y_home_today_schedule_icon)
			)

			Text(
				text = stringResource(R.string.home_schedule_title),
				modifier = Modifier
					.padding(horizontal = 8.dp)
					.align(Alignment.CenterVertically),
				fontWeight = FontWeight.Bold,
				color = colorResource(R.color.green),
				fontSize = 18.sp
			)
		}

		when(schedule) {
			is ViewResource.Loading -> Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
			) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center)
				)
			}

			is ViewResource.Success -> if(schedule.value.isEmpty()) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(120.dp)
				) {
					Text(
						text = stringResource(R.string.home_no_class_today),
						modifier = Modifier.align(Alignment.Center),
						color = colorResource(R.color.gray)
					)
				}
			}else{
				Spacer(modifier = Modifier.height(8.dp))

				LazyColumn {
					items(schedule.value) {
						ScheduleItem(item = it, now = now)
					}
				}
			}

			is ViewResource.Error -> Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
			) {
				RefreshButton(
					onClick = {
						// TODO
					}
				)
			}
		}
	}
}

private val TimeColumnWidth = 50.dp
private val ClassroomColumnWidth = 60.dp

@Composable
fun ScheduleItem(item: Timetable.Entry, now: ZonedDateTime) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Column(
			modifier = Modifier.width(TimeColumnWidth),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// TODO respect timezone
			val time = periodToTime(item.time)

			Text(
				text = time?.toString() ?: "N/A",
				fontSize = 14.sp
			)

			val start = periodToTime(item.time)
			val end = periodToTime(item.time + item.length)

			if(start != null && end != null
				&& (start.hour < now.hour || start.hour == now.hour && start.minute <= now.minute)
				&& (now.hour < end.hour || end.hour == now.hour && now.minute <= end.minute)
			) {
				val alpha by blinkTransition()
				Box(
					modifier = Modifier
						.size(8.dp)
						.background(
							colorResource(R.color.red).copy(alpha = alpha),
							shape = CircleShape
						)
				)
			}
		}

		Column(
			modifier = Modifier
				.weight(1f)
				.padding(horizontal = 16.dp)
		) {
			Text(
				text = item.subjectName,
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)

			Text(
				text = item.professor ?: "",
				fontSize = 14.sp
			)
		}

		Text(
			text = item.classroom,
			modifier = Modifier
				.align(Alignment.CenterVertically)
				.width(ClassroomColumnWidth),
			fontSize = 14.sp,
			textAlign = TextAlign.Center
		)
	}
}
