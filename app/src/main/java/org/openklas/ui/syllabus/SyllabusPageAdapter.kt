package org.openklas.ui.syllabus

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

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.openklas.ui.syllabus.page.achievement.AchievementFragment
import org.openklas.ui.syllabus.page.schedule.ScheduleFragment
import org.openklas.ui.syllabus.page.summary.SummaryFragment

class SyllabusPageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
	override fun getItemCount(): Int {
		return 3
	}

	override fun createFragment(position: Int): Fragment = when(position) {
		0 -> SummaryFragment()
		1 -> AchievementFragment()
		2 -> ScheduleFragment()
		else -> throw IllegalStateException("Invalid position $position given")
	}
}
