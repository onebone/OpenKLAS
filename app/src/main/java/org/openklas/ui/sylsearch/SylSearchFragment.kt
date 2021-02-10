package org.openklas.ui.sylsearch

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.SyllabusSearchFragmentBinding
import org.openklas.widget.AppbarView

@AndroidEntryPoint
class SylSearchFragment: BaseFragment() {
	private lateinit var viewModel: SylSearchViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.syllabus_search), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		val binding = SyllabusSearchFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@SylSearchFragment
		}

		viewModel = getViewModel()

		binding.viewModel = viewModel
		binding.v = this
		binding.rvSyllabus.adapter = SylSearchAdapter()

		val motionRoot = binding.motionRoot
		val backdropTransition = motionRoot.getTransition(R.id.transition_backdrop)

		setAppbarOnClickSearchListener { _, cancel ->
			motionRoot.transitionToState(if(cancel) R.id.set_backdrop_open else R.id.set_backdrop_closed)
		}

		motionRoot.transitionToState(R.id.set_backdrop_open)
		motionRoot.setTransitionListener(object: MotionLayout.TransitionListener {
			override fun onTransitionStarted(v: MotionLayout, begin: Int, end: Int) {}

			override fun onTransitionChange(v: MotionLayout, begin: Int, end: Int, progress: Float) {}

			override fun onTransitionCompleted(v: MotionLayout, state: Int) {
				if(state == R.id.set_backdrop_open) {
					backdropTransition.setEnable(true)
					setAppbarSearchState(false)
				}else if(state == R.id.set_backdrop_closed) {
					backdropTransition.setEnable(false)
					setAppbarSearchState(true)
				}
			}

			override fun onTransitionTrigger(v: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {}
		})

		return binding.root
	}
}
