package org.openklas.ui.shared

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

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.openklas.R
import org.openklas.base.PermissionHolder
import org.openklas.base.list.SimpleDiffUtil
import org.openklas.databinding.ItemAttachmentBinding
import org.openklas.klas.KlasUri
import org.openklas.klas.model.Attachment
import java.net.URL

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

	private fun downloadFile(context: Context, url: String, fileName: String) {
		val targetUrl = URL(URL(KlasUri.ROOT_URI), url).toString()

		val request = DownloadManager.Request(Uri.parse(targetUrl)).apply {
			setTitle(fileName)
			setDescription(context.resources.getString(R.string.download_description))
			setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
			setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
		}

		val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		if(manager.enqueue(request) == 0L) {
			Toast.makeText(context, R.string.download_unavailable, Toast.LENGTH_LONG).show()
		}
	}
}