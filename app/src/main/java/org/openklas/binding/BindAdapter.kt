package org.openklas.binding

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.openklas.base.recycler.ViewTypeRecyclerAdapter


object BindAdapter {
	@JvmStatic
	@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
	fun bindImage(imageView: ImageView, url: String?, placeholder: Drawable?) {
		Glide.with(imageView.context).load(url).apply(RequestOptions.placeholderOf(placeholder))
			.into(imageView)
	}

	@JvmStatic
	@BindingAdapter(value = ["circleImageUrl", "placeHolder"], requireAll = false)
	fun bindCircleImage(imageView: ImageView, url: String?, placeholder: Drawable?) {
		Glide.with(imageView.context).load(url)
			.apply(RequestOptions.circleCropTransform().placeholder(placeholder)).into(imageView)
	}

	@JvmStatic
	@BindingAdapter("items")
	fun <T> bindItems(recyclerView: RecyclerView, list: Array<T>?) {
		recyclerView.adapter?.let {
			if (recyclerView.adapter is ViewTypeRecyclerAdapter<*>) {
				(recyclerView.adapter as ViewTypeRecyclerAdapter<T>).setItems(list?.toList())
			}
		}

	}
}
