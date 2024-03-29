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

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.OnlineContentEntry
import java.time.ZonedDateTime
import me.onebone.openklas.utils.ViewResource

@Composable
fun AssignmentFrame(
	assignments: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Homework>>>,
	impending: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Homework>>>,
	onAssignmentRefresh: () -> Unit,
	now: ZonedDateTime
) {
	OnlineContentListFrame(
		icon = painterResource(R.drawable.ic_tear),
		iconDescription = stringResource(R.string.a11y_home_homework_icon),
		title = stringResource(R.string.home_homework_title),
		items = assignments,
		impending = impending,
		onOnlineContentsRefresh = onAssignmentRefresh,
		now = now,
		noOnlineContent = stringResource(R.string.home_no_homework)
	)
}

@Composable
fun OnlineVideoFrame(
	videos: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Video>>>,
	impending: ViewResource<List<Pair<BriefSubject, OnlineContentEntry.Video>>>,
	onOnlineVideoRefresh: () -> Unit,
	now: ZonedDateTime
) {
	OnlineContentListFrame(
		icon = painterResource(R.drawable.ic_tear),
		iconDescription = stringResource(R.string.a11y_home_online_video_icon),
		title = stringResource(R.string.home_online_video_title),
		noOnlineContent = stringResource(R.string.home_no_video),
		items = videos,
		impending = impending,
		onOnlineContentsRefresh = onOnlineVideoRefresh,
		now = now
	)
}
