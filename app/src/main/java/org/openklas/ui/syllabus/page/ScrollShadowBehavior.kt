package org.openklas.ui.syllabus.page

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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import org.openklas.R

class ScrollShadowBehavior: CoordinatorLayout.Behavior<View> {
	@Suppress("unused")
	constructor(): super()

	@Suppress("unused")
	constructor(context: Context, attrs: AttributeSet): super(context, attrs)

	override fun layoutDependsOn(
		parent: CoordinatorLayout,
		child: View,
		dependency: View
	): Boolean {
		return dependency.id == R.id.shadow
	}

	override fun onStartNestedScroll(
		coordinatorLayout: CoordinatorLayout,
		child: View,
		directTargetChild: View,
		target: View,
		axes: Int,
		type: Int
	): Boolean {
		return axes == ViewCompat.SCROLL_AXIS_VERTICAL
	}

	override fun onNestedScroll(
		coordinatorLayout: CoordinatorLayout,
		child: View,
		target: View,
		dxConsumed: Int,
		dyConsumed: Int,
		dxUnconsumed: Int,
		dyUnconsumed: Int,
		type: Int,
		consumed: IntArray
	) {
		val shadow = getFirstShadow(coordinatorLayout.getDependencies(child)) ?: return

		if(child.canScrollVertically(-1) || child.scrollY > 0) {
			shadow.visibility = View.VISIBLE
		}else{
			shadow.visibility = View.GONE
		}
	}

	private fun getFirstShadow(dependencies: List<View>): View? {
		return dependencies.find { it.id == R.id.shadow }
	}
}
