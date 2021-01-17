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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.PostListFragmentBinding
import org.openklas.ui.common.ActivityViewModel
import org.openklas.widget.TitleView

@AndroidEntryPoint
class PostListFragment : BaseFragment<PostListFragmentBinding>() {
	private val postListArgs by navArgs<PostListFragmentArgs>()
	private val viewModel: PostListViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = createAndBindView(inflater, R.layout.post_list_fragment, container)

		prepareViewModel(viewModel)
		if(!viewModel.hasQuery()) {
			viewModel.setQuery("", "", postListArgs.type, 1)
		}

		mBinding.rvPosts.adapter = PostListAdapter()

		mBinding.viewModel = viewModel

		return view
	}

	override fun onResume() {
		super.onResume()

		val activityViewModel: ActivityViewModel by activityViewModels()
		activityViewModel.title.value = resources.getString(when(postListArgs.type) {
			null -> R.string.post_list_title
			PostType.NOTICE -> R.string.course_notice
			PostType.LECTURE_MATERIAL -> R.string.course_material
			PostType.QNA -> R.string.course_qna
		})
		activityViewModel.titleHeaderType.value = TitleView.HeaderType.BACK
	}
}
