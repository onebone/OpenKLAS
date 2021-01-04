package org.openklas.ui.home

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

import androidx.annotation.LayoutRes
import org.openklas.R

enum class HomeViewType(
	@LayoutRes val layout: Int
) {
	SCHEDULE(R.layout.home_schedule_root_item),
	HOMEWORK(R.layout.home_homework_root_item),
	ONLINE_CONTENTS(R.layout.home_online_video_root_item)
}
