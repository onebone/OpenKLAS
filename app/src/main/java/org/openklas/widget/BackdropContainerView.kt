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

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import org.openklas.R

class BackdropContainerView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
): MotionLayout(context, attrs, defStyleAttr) {
	private var isMotionLayoutTraversing = false

	override fun onFinishInflate() {
		super.onFinishInflate()

		val interceptor = BackdropTouchInterceptor(context).apply {
			id = R.id.backdrop_interceptor
		}

		// interceptor view must be in the index of 0 to receive its touch event at last
		findViewById<BackdropFrontView>(R.id.layout_layer_front)
			?.addView(interceptor, 0, LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		super.onLayout(changed, left, top, right, bottom)

		val frontView = findViewById<BackdropFrontView>(R.id.layout_layer_front)
		frontView?.findViewById<View>(R.id.backdrop_content_divider)?.let { divider ->
			getConstraintSet(R.id.set_backdrop_open).apply {
				// use negative margin when ConstraintLayout supports (>= 2.1.0)
				setMargin(R.id.layout_layer_front, ConstraintSet.TOP, measuredHeight - divider.top)
			}
		}
	}

	override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
		isMotionLayoutTraversing = true
		val intercepted = super.onInterceptTouchEvent(event)
		isMotionLayoutTraversing = false

		return intercepted
	}

	internal inner class BackdropTouchInterceptor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = 0
	): View(context, attrs, defStyleAttr) {
		@SuppressLint("ClickableViewAccessibility")
		override fun onTouchEvent(event: MotionEvent): Boolean {
			// MotionLayout traverses its children by itself if touchRegionId attribute is set
			// and animates its motion if none of the children's onTouchEvent() returns true.
			// (Note that returning true in onTouchEvent() indicates that the touch is consumed)
			// However if none of the children of view associated with touchRegionId returned true,
			// Android framework passes MotionEvent to the View that is behind the layer. This leads
			// to the behavior where backdrop's front view ignores user's touch. This will allow
			// EditText or Buttons in the behind layer of the backdrop to be touched.
			// To deal with this glitch, this interceptor view only consumes user's touch only when
			// Android framework is traversing through the children. By ignoring call of onTouchEvent()
			// from the MotionLayout, MotionLayout will be able to process its motion and user will not
			// be able to touch the view in the back layer.
			return !isMotionLayoutTraversing
		}
	}
}
