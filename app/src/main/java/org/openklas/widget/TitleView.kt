package org.openklas.widget

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

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.openklas.R

class TitleView: FrameLayout {
	private lateinit var tvTitle: TextView
	private lateinit var imgHeader: ImageView
	private lateinit var imgSearch: ImageView

	var title: String = ""
		set(value) {
			tvTitle.text = value
			field = value
		}

	var headerType: HeaderType = HeaderType.HAMBURGER
		set(value) {
			if(value == field) return

			var state = intArrayOf()
			state += if(value == HeaderType.NONE) {
				imgHeader.visibility = View.INVISIBLE
				R.attr.icon_state_hidden
			}else{
				imgHeader.visibility = View.VISIBLE
				-R.attr.icon_state_hidden
			}

			if(value == HeaderType.HAMBURGER) {
				state += -R.attr.icon_state_back
			}else if(value == HeaderType.BACK) {
				state += R.attr.icon_state_back
			}

			imgHeader.setImageState(state, true)

			field = value
		}

	var searchType: SearchType = SearchType.SEARCH
		set(value) {
			if(field == value) return

			var state = intArrayOf()
			state += if(value == SearchType.NONE) {
				imgSearch.visibility = View.INVISIBLE
				R.attr.icon_state_hidden
			}else{
				imgSearch.visibility = View.VISIBLE
				-R.attr.icon_state_hidden
			}

			if(value == SearchType.SEARCH) {
				state += -R.attr.icon_state_cancel
			}else if(value == SearchType.CANCEL) {
				state += R.attr.icon_state_cancel
			}

			imgSearch.setImageState(state, true)

			field = value
		}

	var onClickBackListener: OnClickBackListener? = null
	var onClickSearchListener: OnClickSearchListener? = null

	constructor(context: Context): super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?, defAttrStyle: Int): super(context, attrs, defAttrStyle) {
		init()
	}

	private fun init() {
		inflate(context, R.layout.title_view, this)
	}

	override fun onFinishInflate() {
		super.onFinishInflate()

		tvTitle = findViewById(R.id.tv_title)
		imgHeader = findViewById<ImageView>(R.id.img_header).also {
			it.setOnClickListener { v ->
				if(headerType == HeaderType.BACK) {
					onClickBackListener?.onClickBack(v)
				}
			}
		}
		imgSearch = findViewById<ImageView>(R.id.img_search).also {
			it.setOnClickListener { v ->
				if(searchType == SearchType.SEARCH) {
					onClickSearchListener?.onClickSearch(v, true)
					searchType = SearchType.CANCEL
				}else if(searchType == SearchType.CANCEL) {
					onClickSearchListener?.onClickSearch(v, false)
					searchType = SearchType.SEARCH
				}
			}
		}
	}

	fun interface OnClickBackListener {
		fun onClickBack(view: View)
	}

	fun interface OnClickSearchListener {
		fun onClickSearch(view: View, isSearch: Boolean)
	}

	companion object {
		@JvmStatic
		@BindingAdapter("title")
		fun bindTitle(view: TitleView, title: String) {
			view.title = title
		}

		@JvmStatic
		@BindingAdapter("headerType")
		fun bindHeaderType(view: TitleView, type: HeaderType) {
			view.headerType = type
		}

		@JvmStatic
		@BindingAdapter("searchType")
		fun bindSearchType(view: TitleView, type: SearchType) {
			view.searchType = type
		}

		@JvmStatic
		@BindingAdapter("onClickBack")
		fun bindOnClickBackListener(view: TitleView, listener: OnClickBackListener?) {
			view.onClickBackListener = listener
		}

		@JvmStatic
		@BindingAdapter("onClickSearch")
		fun bindOnClickSearchListener(view: TitleView, listener: OnClickSearchListener?) {
			view.onClickSearchListener = listener
		}
	}

	enum class SearchType {
		NONE, SEARCH, CANCEL
	}

	enum class HeaderType {
		NONE, HAMBURGER, BACK
	}
}
