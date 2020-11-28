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
import org.greenrobot.eventbus.EventBus
import org.openklas.R
import org.openklas.databinding.TitleViewBinding
import org.openklas.event.ControlDrawerEvent
import org.openklas.event.Mode
import org.openklas.event.SearchEvent
import pyxis.uzuki.live.attribute.parser.TitleViewAttributes
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt
import pyxis.uzuki.live.attribute.parser.annotation.AttrString
import pyxis.uzuki.live.attribute.parser.annotation.CustomView

@CustomView
class TitleView: LinearLayout {
	@AttrString
	var titleName: String? = null

	@AttrInt
	var titleMode = 0

	private var mBinding: TitleViewBinding? = null

	private var mOnClickBackListener: OnClickBackListener? = null

	private var mOnClickMypageListner: OnClickMypageListener? = null

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
		EventBus.getDefault().post(SearchEvent(mKeyword.get()))
	}

	fun clickBack(view: View) {
		if (mOnClickBackListener != null) {
			mOnClickBackListener!!.onClickBack(view)
		}
		//		else {
//			BaseActivity activity = (BaseActivity) getContext();
//			activity.finish();
//		}
	}

	fun clickMypage(view: View) {
		if (mOnClickMypageListner != null) {
			mOnClickMypageListner!!.onClickMypage(view)
		} else {
			EventBus.getDefault().post(ControlDrawerEvent(Mode.Open))
		}
	}

	fun setOnClickBackListener(onClickBackListener: OnClickBackListener?) {
		mOnClickBackListener = onClickBackListener
	}

	fun setOnClickMypageListner(onClickMypageListner: OnClickMypageListener?) {
		mOnClickMypageListner = onClickMypageListner
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
		fun onClickBack(view: View?)
	}

	interface OnClickMypageListener {
		fun onClickMypage(view: View?)
	}

	companion object {
		@BindingAdapter("bindTitle")
		fun bindTitleName(view: TitleView, title: String?) {
			view.setTitle(title)
		}

		@BindingAdapter("bindMode")
		fun bindTitleMode(view: TitleView, mode: Int) {
			view.setMode(mode)
		}

		@BindingAdapter("onClickBack")
		fun bindClickBack(view: TitleView, listener: OnClickBackListener?) {
			view.setOnClickBackListener(listener)
		}

		@JvmStatic
		@BindingAdapter("onClickMypage")
		fun bindClickBack(view: TitleView, listener: OnClickMypageListener?) {
			view.setOnClickMypageListner(listener)
		}
	}
}