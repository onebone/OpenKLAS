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

package me.onebone.openklas.ui.assignment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.klas.model.Attachment
import me.onebone.openklas.utils.downloadFile

@AndroidEntryPoint
class AssignmentFragment: BaseFragment() {
	private val args by navArgs<AssignmentFragmentArgs>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val viewModel by viewModels<AssignmentViewModel>()

		viewModel.fetchAssignment(args.semester, args.subject, args.order)

		val onDownloadAttachment: (Attachment) -> Unit = { attachment ->
			permissionHolder!!.askPermissionAndDo(
				permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
				executor = {
					downloadFile(requireContext(), attachment.url, attachment.fileName)
				}
			)
		}

		return ComposeView(requireContext()).apply {
			setContent {
				MaterialTheme {
					AssignmentScreen(
						onDownloadAttachment = onDownloadAttachment
					)
				}
			}
		}
	}
}
