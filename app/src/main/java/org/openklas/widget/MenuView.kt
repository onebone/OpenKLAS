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
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import org.openklas.R
import org.openklas.databinding.MenuViewBinding

class MenuView: LinearLayout {
	private var mBinding: MenuViewBinding? = null

	constructor(context: Context?): super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
			: super(context, attrs, defStyleAttr) {
		init()
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	): super(context, attrs, defStyleAttr, defStyleRes) {
		init()
	}

	private fun init() {
		if (isInEditMode) {
			inflate(context, R.layout.menu_view, this)
			return
		}
		val inflater = LayoutInflater.from(context)
		mBinding = DataBindingUtil.inflate(inflater, R.layout.menu_view, this, true)
		mBinding!!.view = this

//      if (attrs != null) {
//          TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MenuView);
//          MenuViewAttributes.apply(this, array);
//      }
		mBinding!!.notifyChange()
	}
}
