package org.openklas.ui.notification

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
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.NotificationFragmentBinding

@AndroidEntryPoint
class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return createAndBindView(inflater, R.layout.notification_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<NotificationViewModel>()
		mBinding.viewModel = viewModel
	}
}
