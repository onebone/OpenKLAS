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

package me.onebone.openklas.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.databinding.PostFragmentBinding
import me.onebone.openklas.klas.model.PostType
import me.onebone.openklas.ui.shared.AttachmentAdapter
import me.onebone.openklas.ui.shared.SimpleHtml
import me.onebone.openklas.ui.shared.compose.base.KlasTheme
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class PostFragment: BaseFragment() {
	private val args by navArgs<PostFragmentArgs>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(
			when(args.postType) {
				PostType.NOTICE -> R.string.course_notice
				PostType.LECTURE_MATERIAL -> R.string.course_material
				PostType.QNA -> R.string.course_qna
			}
		), AppbarView.HeaderType.BACK, AppbarView.SearchType.NONE)

		val viewModel by viewModels<PostViewModel>()

		val binding = PostFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = viewLifecycleOwner
		}.apply {
			cvPostContent.setContent {
				KlasTheme {
					val content = viewModel.post.observeAsState()
					content.value?.content?.let {
						SelectionContainer {
							SimpleHtml(html = it)
						}
					}
				}
			}

			this.viewModel = viewModel
			rvAttachments.adapter = AttachmentAdapter(permissionHolder!!)
		}

		viewModel.setSubject(args.subject)
		viewModel.setCurrentSemester(args.semester)
		viewModel.fetchPost(args.semester, args.subject, args.postType, args.boardNo, args.masterNo)

		return binding.root
	}
}
