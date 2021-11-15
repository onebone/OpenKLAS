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

package me.onebone.openklas.ui.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.databinding.PostListFragmentBinding
import me.onebone.openklas.klas.model.PostType
import me.onebone.openklas.klas.request.BoardSearchCriteria
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class PostListFragment: BaseFragment() {
	private val args by navArgs<PostListFragmentArgs>()
	private val viewModel: PostListViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val fragment = childFragmentManager.findFragmentByTag(SearchBottomSheetFragmentTag)
		if(fragment is SearchBottomSheetFragment) {
			fragment.registerSearchEventHandler()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(when(args.type) {
			PostType.NOTICE -> R.string.course_notice
			PostType.LECTURE_MATERIAL -> R.string.course_material
			PostType.QNA -> R.string.course_qna
		}), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		val binding = PostListFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}

		binding.viewModel = viewModel
		binding.v = this

		binding.tvChangeSubject.setOnClickListener {
			PostListSubjectSelectionDialog().show(childFragmentManager, null)
		}

		prepareViewModel(viewModel)
		viewModel.setPostType(args.type)

		val adapter = PostListAdapter {
			// semester and subject must be resolved at this time
			val semester = viewModel.currentSemester.value ?: return@PostListAdapter
			val subject = viewModel.currentSubject.value ?: return@PostListAdapter

			findNavController().navigate(PostListFragmentDirections.actionPostlsPost(
				args.type, it.boardNo, it.masterNo, semester.id, subject.id
			))
		}

		binding.rvPosts.adapter = adapter

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.posts.collectLatest {
						adapter.submitData(it)
					}
				}

				// no way to bind loadStateFlow in view model??
				launch {
					adapter.loadStateFlow.distinctUntilChangedBy { it.refresh }.collectLatest {
						if(it.refresh is LoadState.Loading) {
							binding.shimmerBackLayer.visibility = View.VISIBLE
							binding.rvPosts.visibility = View.INVISIBLE
						}else{
							binding.shimmerBackLayer.visibility = View.GONE
							binding.rvPosts.visibility = View.VISIBLE
						}
					}
				}
			}
		}

		setAppbarOnClickSearchListener { _, cancel ->
			if(cancel) {
				val dialog = SearchBottomSheetFragment()
				dialog.registerSearchEventHandler()
				dialog.show(childFragmentManager, SearchBottomSheetFragmentTag)
			}
		}

		return binding.root
	}

	private fun SearchBottomSheetFragment.registerSearchEventHandler() {
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				// we only get a single event from flow
				val event = flow.first()
				if(event is SearchEvent) {
					// TODO implement changing search criteria
					viewModel.setFilter(BoardSearchCriteria.ALL, event.keyword)
				}

				setAppbarSearchState(true)
				dismiss()
			}
		}
	}

	companion object {
		private const val SearchBottomSheetFragmentTag = "PostListSearchBottomSheetFragment"
	}
}
