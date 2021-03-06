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

package org.openklas.ui.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import kotlinx.coroutines.launch
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.PostListFragmentBinding
import org.openklas.klas.model.PostType
import org.openklas.klas.request.BoardSearchCriteria
import org.openklas.widget.AppbarView

@AndroidEntryPoint
class PostListFragment: BaseFragment() {
	private val args by navArgs<PostListFragmentArgs>()
	private val viewModel: PostListViewModel by viewModels()

	private lateinit var binding: PostListFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(when(args.type) {
			PostType.NOTICE -> R.string.course_notice
			PostType.LECTURE_MATERIAL -> R.string.course_material
			PostType.QNA -> R.string.course_qna
		}), AppbarView.HeaderType.BACK, AppbarView.SearchType.SEARCH)

		binding = PostListFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@PostListFragment
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

		binding.etKeyword.setOnEditorActionListener { v, actionId, _ ->
			return@setOnEditorActionListener when(actionId) {
				EditorInfo.IME_ACTION_SEND -> {
					onClickFilterSubmit(v.text.toString())
					true
				}
				else -> false
			}
		}

		val contentRoot = binding.contentRoot
		setAppbarOnClickSearchListener { _, cancel ->
			if(cancel) {
				contentRoot.collapseBottomSheet()
			}else{
				contentRoot.expandBottomSheet()
			}
		}

		contentRoot.setOnCollapsedListener {
			binding.etKeyword.isEnabled = true
			setAppbarSearchState(false)
		}

		contentRoot.setOnExpandedListener {
			binding.etKeyword.isEnabled = false
			setAppbarSearchState(true)
		}

		return binding.root
	}

	fun onClickFilterSubmit(keyword: String) {
		// TODO implement changing search criteria
		viewModel.setFilter(BoardSearchCriteria.ALL, keyword)

		binding.contentRoot.expandBottomSheet()
	}
}
