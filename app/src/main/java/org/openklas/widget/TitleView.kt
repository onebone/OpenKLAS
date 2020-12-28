package org.openklas.widget

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

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.github.windsekirun.bindadapters.observable.ObservableString
import org.openklas.R
import org.openklas.databinding.TitleViewBinding
import pyxis.uzuki.live.attribute.parser.TitleViewAttributes
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt
import pyxis.uzuki.live.attribute.parser.annotation.AttrString
import pyxis.uzuki.live.attribute.parser.annotation.CustomView

@CustomView
class TitleView: LinearLayout {
	@JvmField
	@AttrString
	var titleName: String? = null

	@JvmField
	@AttrInt
	var titleMode = 0

	private var mBinding: TitleViewBinding? = null

	var onClickBackListener: OnClickBackListener? = null
	var onClickMypageListener: OnClickMypageListener? = null
	var onClickSearchListener: OnClickSearchListener? = null

	var mTitle = ObservableString("")
	var mMode = ObservableInt(0)
	var isEditTextOpen = ObservableBoolean(false)
	var mKeyword = ObservableString()

	constructor(context: Context): super(context) {
		init(null)
	}

	constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
		init(attrs)
	}

	fun clickSearch(view: View) {
		onClickSearchListener?.onClickSearch(view, mKeyword.get())
	}

	fun clickBack(view: View) {
		onClickBackListener?.onClickBack(view)
	}

	fun clickMypage(view: View) {
		onClickMypageListener?.onClickMypage(view)
	}

	private fun init(attrs: AttributeSet?) {
		val inflater = LayoutInflater.from(context)
		mBinding = DataBindingUtil.inflate(inflater, R.layout.title_view, this, true)
		mBinding!!.view = this
		if (attrs != null) {
			val array = context.obtainStyledAttributes(attrs, R.styleable.TitleView)
			TitleViewAttributes.apply(this, array)
		}

		if (titleName != null) mTitle.set(titleName!!)

		mMode.set(titleMode)
		
		mBinding!!.notifyChange()
	}

	fun setTitle(title: String?) {
		mTitle.set(title!!)
	}

	fun setMode(mode: Int) {
		mMode.set(mode)
	}

	interface OnClickBackListener {
		fun onClickBack(view: View)
	}

	interface OnClickMypageListener {
		fun onClickMypage(view: View)
	}

	interface OnClickSearchListener {
		fun onClickSearch(view: View, keyword: String)
	}

	companion object {
		@JvmStatic
		@BindingAdapter("bindTitle")
		fun bindTitleName(view: TitleView, title: String?) {
			view.setTitle(title)
		}

		@JvmStatic
		@BindingAdapter("bindMode")
		fun bindTitleMode(view: TitleView, mode: Int) {
			view.setMode(mode)
		}

		@JvmStatic
		@BindingAdapter("onClickBack")
		fun bindClickBack(view: TitleView, listener: OnClickBackListener?) {
			view.onClickBackListener = listener
		}

		@JvmStatic
		@BindingAdapter("onClickMypage")
		fun bindClickMypage(view: TitleView, listener: OnClickMypageListener?) {
			view.onClickMypageListener = listener
		}

		@JvmStatic
		@BindingAdapter("onClickSearch")
		fun bindOnClickSearch(view: TitleView, listener: OnClickSearchListener?) {
			view.onClickSearchListener = listener
		}
	}
}
