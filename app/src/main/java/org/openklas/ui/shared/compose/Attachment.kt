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

package org.openklas.ui.shared.compose

import android.text.format.Formatter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.openklas.R
import org.openklas.klas.model.Attachment

@Composable
fun AttachmentList(attachments: Array<Attachment>, onClickEntry: (Attachment) -> Unit) {
	LazyColumn(modifier = Modifier.fillMaxWidth()) {
		items(attachments, key = { it.order }) {
			AttachmentEntry(it, onClick = onClickEntry)
		}
	}
}

@Composable
fun AttachmentEntry(attachment: Attachment, onClick: (Attachment) -> Unit) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onClick(attachment) }
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			painter = painterResource(R.drawable.ic_sharp_attach_file_24),
			tint = colorResource(R.color.primary),
			contentDescription = null
		)

		Spacer(modifier = Modifier.size(6.dp, 0.dp))

		Column {
			Text(
				text = attachment.fileName,
				fontSize = 16.sp
			)

			Text(
				text = Formatter.formatFileSize(LocalContext.current, attachment.fileSize),
				color = colorResource(R.color.file_size)
			)
		}
	}
}
