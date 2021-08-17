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

package me.onebone.openklas.ui.shared

import android.Manifest
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.onebone.openklas.R
import me.onebone.openklas.base.PermissionHolder
import me.onebone.openklas.base.list.SimpleDiffUtil
import me.onebone.openklas.databinding.ItemAttachmentBinding
import me.onebone.openklas.klas.model.Attachment
import me.onebone.openklas.utils.downloadFile

class AttachmentAdapter(
	private val permissionHolder: PermissionHolder
): ListAdapter<Attachment, AttachmentAdapter.ViewHolder>(SimpleDiffUtil { it.order }) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class ViewHolder(val binding: ItemAttachmentBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(entry: Attachment) {
			binding.entry = entry

			binding.root.setOnClickListener {
				permissionHolder.askPermissionAndDo(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), executor = {
					downloadFile(binding.root.context, entry.url, entry.fileName)
				}, deniedExecutor = {
					Toast.makeText(binding.root.context, R.string.permission_download_denied, Toast.LENGTH_SHORT).show()
				})
			}
		}
	}
}
