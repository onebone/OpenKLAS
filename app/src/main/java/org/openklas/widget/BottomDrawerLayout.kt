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

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import org.openklas.R
import org.openklas.utils.dp2px
import kotlin.math.max

class BottomDrawerLayout: ViewGroup {
	var lastMotionY = 0f
	var bottomEdgeSize = dp2px(30f, context)

	private var isAnimating = false

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

		var consumedHeight = 0

		val childCount = childCount
		for(i in 0 until childCount) {
			val view = getChildAt(i)
			if(view.visibility == View.GONE) continue

			val lp = view.layoutParams as LayoutParams
			if(lp.isBottom) {
				if(hasBottom)
					throw IllegalStateException("BottomDrawerLayout cannot have more than two bottom layout")

				val onScreen = lp.onScreen
				val measuredHeight = measuredHeight

				val childHeight = view.measuredHeight

				val childVisibleHeight = (childHeight * onScreen).toInt()

				view.layout(lp.leftMargin, lp.topMargin + measuredHeight - childVisibleHeight,
					lp.leftMargin + view.measuredWidth, lp.topMargin + measuredHeight + childHeight - childVisibleHeight)

				hasBottom = true
			}else{
				val childWidth = view.measuredWidth
				val childHeight = view.measuredHeight

				view.layout(lp.leftMargin, lp.topMargin + consumedHeight,
					lp.leftMargin + childWidth, lp.topMargin + childHeight + consumedHeight)

				consumedHeight += childHeight + lp.topMargin + lp.bottomMargin
			}
		}
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		val y = ev.y

		if(y > measuredHeight - bottomEdgeSize) return true

		val bottom = findBottomView()
		if(bottom != null) {
			val lp = bottom.layoutParams as LayoutParams
			if(lp.onScreen == 1f && y < bottom.top) { // touched outside of bottom view when it is open
				return true
			}
		}

		return super.onInterceptTouchEvent(ev)
	}

	fun openBottomView() {
		val bottom = findBottomView() ?: return

		ValueAnimator.ofFloat(0f, 1f).apply {
			setupAndStartAnimator(bottom, this)
		}
	}

	fun closeBottomView() {
		val bottom = findBottomView() ?: return

		ValueAnimator.ofFloat(1f, 0f).apply {
			setupAndStartAnimator(bottom, this)
		}
	}

	private fun setupAndStartAnimator(bottom: View, animator: ValueAnimator) {
		val lp = bottom.layoutParams as LayoutParams

		animator.addUpdateListener {
			lp.onScreen = animator.animatedValue as Float

			bottom.layoutParams = lp
		}

		animator.doOnStart {
			isAnimating = true
		}

		animator.doOnEnd {
			isAnimating = false
		}

		animator.duration = 500
		animator.start()
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		when(event.actionMasked) {
			MotionEvent.ACTION_DOWN -> {
				val y = event.y

				val height = measuredHeight

				val bottom = findBottomView()
				if(bottom != null) {
					val lp = bottom.layoutParams as LayoutParams

					if(lp.onScreen == 1f && y < bottom.top) {
						closeBottomView()
						return true
					}
				}

				if(y > height - bottomEdgeSize) {
					lastMotionY = event.y
					return true
				}
			}
			MotionEvent.ACTION_MOVE -> {
				if(isAnimating) return true

				val dy = event.y - lastMotionY
				println("${event.y}, $lastMotionY, $dy")
				if(dy < 0) { // dragging up
					openBottomView()
					return false
				}

				lastMotionY = event.y

				return true
			}
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
				lastMotionY = 0f
			}
		}

		return super.onTouchEvent(event)
	}

	private fun findBottomView(): View? {
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
		var onScreen: Float = 0f

		init {
			context.obtainStyledAttributes(attrs, R.styleable.BottomDrawerLayout_Layout).apply {
				isBottom = this.getBoolean(R.styleable.BottomDrawerLayout_Layout_bottom_view, false)
			}.recycle()
		}
	}
}
