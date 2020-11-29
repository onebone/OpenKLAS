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
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import org.openklas.R
import kotlin.math.max

class BottomDrawerLayout: ViewGroup {
	var lastMotionY = -1f

	constructor(context: Context): super(context)
	constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
			super(context, attrs, defStyleAttr)

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val childCount = childCount

		var totalWidth = 0
		var totalHeight = 0

		var childState = 0

		for(i in 0 until childCount) {
			val child = getChildAt(i)
			measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

			val lp = child.layoutParams as LayoutParams
			if(!lp.isBottom) {
				totalWidth += child.measuredWidth + lp.leftMargin + lp.rightMargin
				totalHeight += child.measuredHeight + lp.topMargin + lp.bottomMargin
			}

			childState = combineMeasuredStates(childState, child.measuredState)
		}

		setMeasuredDimension(
			resolveSizeAndState(max(totalWidth, suggestedMinimumWidth), widthMeasureSpec, childState),
			resolveSizeAndState(max(totalHeight, suggestedMinimumHeight), heightMeasureSpec, childState shl MEASURED_HEIGHT_STATE_SHIFT)
		)
	}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		var hasBottom = false

		var consumedWidth = 0
		var consumedHeight = 0

		val childCount = childCount
		for(i in 0 until childCount) {
			val view = getChildAt(i)
			if(view.visibility == View.GONE) continue

			val lp = view.layoutParams as LayoutParams
			if(lp.isBottom) {
				if(hasBottom)
					throw IllegalStateException("BottomDrawerLayout cannot have more than two bottom layout")

				val measuredHeight = measuredHeight

				view.layout(lp.leftMargin, lp.topMargin + measuredHeight - 150,
					lp.leftMargin + view.measuredWidth, lp.topMargin + measuredHeight + view.measuredHeight)

				hasBottom = true
			}else{
				val childWidth = view.measuredWidth
				val childHeight = view.measuredHeight

				view.layout(lp.leftMargin + consumedWidth, lp.topMargin + consumedHeight,
					lp.leftMargin + childWidth + consumedWidth, lp.topMargin + childHeight + consumedHeight)

				consumedWidth += childWidth + lp.leftMargin
				consumedHeight += childHeight + lp.topMargin
			}
		}
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		return super.onInterceptTouchEvent(ev)
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		when(event.actionMasked) {
			MotionEvent.ACTION_DOWN -> {
				val y = event.y

				val height = measuredHeight

				if(y > height - 200) { // TODO do not hard code edge height
					lastMotionY = event.y
					return true
				}
			}
			MotionEvent.ACTION_MOVE -> {
				val dy = event.y - lastMotionY
				if(dy < 0) { // dragging up
					findBottomLayout()?.let {
						it.offsetTopAndBottom(measuredHeight - it.bottom)
						// TODO animate sliding up
					}
				}

				lastMotionY = event.y

				return true
			}
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
				lastMotionY = -1f
			}
		}

		return super.onTouchEvent(event)
	}

	private fun findBottomLayout(): View? {
		val childCount = childCount
		for(i in 0 until childCount) {
			val child = getChildAt(i)

			val lp = child.layoutParams as LayoutParams
			if(lp.isBottom) return child
		}

		return null
	}

	override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
		return LayoutParams(context, attrs)
	}

	override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
		return LayoutParams(p)
	}

	override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
		return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
	}

	override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
		return p is LayoutParams
	}

	class LayoutParams(context: Context, attrs: AttributeSet?) : MarginLayoutParams(context, attrs) {
		var isBottom: Boolean = false

		init {
			context.obtainStyledAttributes(attrs, R.styleable.BottomDrawerLayout_Layout).apply {
				isBottom = this.getBoolean(R.styleable.BottomDrawerLayout_Layout_bottom_view, false)
			}.recycle()
		}
	}
}
