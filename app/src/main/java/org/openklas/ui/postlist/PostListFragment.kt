package org.openklas.ui.postlist

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.PostListFragmentBinding
import org.openklas.klas.model.Board
import org.openklas.widget.AppbarView

@AndroidEntryPoint
class PostListFragment: BaseFragment() {
	private val postListArgs by navArgs<PostListFragmentArgs>()
	private val viewModel: PostListViewModel by viewModels()

	private lateinit var binding: PostListFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(when(postListArgs.type) {
			PostType.NOTICE -> R.string.course_notice
			PostType.LECTURE_MATERIAL -> R.string.course_material
			PostType.QNA -> R.string.course_qna
		}), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		binding = PostListFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@PostListFragment
		}

		prepareViewModel(viewModel)
		if(!viewModel.hasQuery()) {
			viewModel.setQuery("", "", postListArgs.type, 1)
		}

		val adapter = PostListAdapter()
		binding.rvPosts.adapter = adapter

		viewModel.posts.observe(viewLifecycleOwner) {
			adapter.submitList(it)
		}

		binding.viewModel = viewModel

		binding.etKeyword.isEnabled = false

		val motionRoot = binding.motionRoot
		val backdropTransition = motionRoot.getTransition(R.id.transition_backdrop)
		backdropTransition.setEnable(false)

		setAppbarOnClickSearchListener { _, cancel ->
			motionRoot.apply {
				transitionToState(
					if(cancel) R.id.set_backdrop_closed
					else R.id.set_backdrop_open
				)
			}
		}

		motionRoot.setTransitionListener(object: MotionLayout.TransitionListener {
			override fun onTransitionStarted(v: MotionLayout, begin: Int, end: Int) {}

			override fun onTransitionChange(v: MotionLayout, begin: Int, end: Int, progress: Float) {}

			override fun onTransitionCompleted(v: MotionLayout, state: Int) {
				if(state == R.id.set_backdrop_closed) {
					binding.etKeyword.isEnabled = true
					backdropTransition.setEnable(true)
					setAppbarSearchState(false)
				}else if(state == R.id.set_backdrop_open) {
					binding.etKeyword.isEnabled = false
					backdropTransition.setEnable(false)
					setAppbarSearchState(true)
				}
			}

			override fun onTransitionTrigger(v: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {}
		})

		return binding.root
	}
}
