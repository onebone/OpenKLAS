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

package org.openklas.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.openklas.R
import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.OnlineContentEntry
import java.time.ZonedDateTime

@Composable
fun AssignmentFrame(
	assignments: List<Pair<BriefSubject, OnlineContentEntry.Homework>>?,
	impending: List<Pair<BriefSubject, OnlineContentEntry.Homework>>?,
	now: ZonedDateTime
) {
	OnlineContentListFrame(
		icon = painterResource(R.drawable.ic_tear),
		iconDescription = stringResource(R.string.a11y_home_homework_icon),
		title = stringResource(R.string.home_homework_title),
		items = assignments,
		impending = impending,
		now = now,
		noOnlineContent = stringResource(R.string.home_no_homework)
	)
}

@Composable
fun OnlineVideoFrame(
	videos: List<Pair<BriefSubject, OnlineContentEntry.Video>>?,
	impending: List<Pair<BriefSubject, OnlineContentEntry.Video>>?,
	now: ZonedDateTime
) {
	OnlineContentListFrame(
		icon = painterResource(R.drawable.ic_tear),
		iconDescription = stringResource(R.string.a11y_home_online_video_icon),
		title = stringResource(R.string.home_online_video_title),
		noOnlineContent = stringResource(R.string.home_no_video),
		items = videos,
		impending = impending,
		now = now
	)
}
