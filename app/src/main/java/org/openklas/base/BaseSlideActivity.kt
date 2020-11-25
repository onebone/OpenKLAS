package org.openklas.base

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import org.greenrobot.eventbus.Subscribe
import org.openklas.R
import org.openklas.databinding.BaseDrawerActivityBinding
import org.openklas.event.ControlDrawerEvent
import org.openklas.event.Mode

abstract class BaseSlideActivity<Origin : ViewDataBinding> : BaseActivity<BaseDrawerActivityBinding>() {

	@JvmField
	protected var originalBinder: Origin? = null

	private val gravity: Int
		get() = drawerOrientation.gravity

	protected val isOpened: Boolean
		get() = mBinding.drawerLayout.isDrawerOpen(gravity)

	protected abstract val drawerOrientation: Orientation
	protected abstract fun createDrawerView(): View
	protected abstract fun getLayoutResId(): Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.base_drawer_activity)
		init(savedInstanceState)
	}

	private fun init(savedInstanceState: Bundle?) {
		val inflater = LayoutInflater.from(this)
		originalBinder = DataBindingUtil.inflate(inflater, getLayoutResId(), null, false);

		val params = DrawerLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
		params.gravity = gravity
		mBinding.drawerFrame.layoutParams = params

		mBinding.drawerFrame.addView(createDrawerView())
		originalBinder?.let { mBinding.contentFrame.addView(it.root) }

		onAfterCreate(savedInstanceState)
	}

	override fun onPause() {
		super.onPause()
		if (isOpened) {
			closeDrawer()
		}
	}

	override fun onBackPressed() {
		if (isOpened) {
			closeDrawer()
			return
		}
		super.onBackPressed()
	}

	protected open fun onAfterCreate(savedInstanceState: Bundle?) {

	}

	protected open fun onClosedDrawer() {

	}

	fun openDrawer() {
		mBinding.drawerLayout.clearFocus()
		mBinding.drawerLayout.openDrawer(gravity)
	}

	fun closeDrawer() {
		onClosedDrawer()
		if (isOpened) {
			mBinding.drawerLayout.closeDrawer(gravity)
		}
	}

	@Subscribe
	fun onControlDrawerEvent(event: ControlDrawerEvent) {
		when (event.mode) {
			Mode.Open -> openDrawer()
			Mode.Close -> closeDrawer()
		}
	}

	enum class Orientation constructor(val gravity: Int) {
		START(GravityCompat.START), END(GravityCompat.END)
	}

}
