package org.openklas.ui.lectureplan

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.greenrobot.eventbus.Subscribe
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.base.impl.port.AlertInterfacePort.showToast
import org.openklas.databinding.LectureplanFragmentBinding
import org.openklas.event.Event

@InjectFragment
class LecturePlanFragment : BaseFragment<LectureplanFragmentBinding>() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = createAndBindView(inflater, R.layout.lectureplan_fragment, container)

		val viewModel = getViewModel<LecturePlanViewModel>()

		mBinding.viewModel = viewModel

		return view
	}

	@Subscribe
	fun getEvent(e: Event) {
		if (e.event) {
			showToast("event hi")
		}
	}
}
