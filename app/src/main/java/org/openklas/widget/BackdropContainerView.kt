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

package org.openklas.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.openklas.R

class BackdropContainerView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
): CoordinatorLayout(context, attrs, defStyleAttr) {
	private var onExpandedListener: SheetExpandedListener? = null
	private var onCollapsedListener: SheetCollapsedListener? = null

	var isInitiallyExpanded = false

	var bottomSheetBehavior: BottomSheetBehavior<BackdropFrontView>? = null
		private set

	init {
		context.obtainStyledAttributes(attrs, R.styleable.BackdropContainerView).apply {
			isInitiallyExpanded = getBoolean(R.styleable.BackdropContainerView_expandInitially, false)
		}.recycle()
	}

	override fun onFinishInflate() {
		super.onFinishInflate()

		findViewById<BackdropFrontView>(R.id.layout_layer_front)?.let { frontView ->
			bottomSheetBehavior = BottomSheetBehavior.from(frontView).also {
				it.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
					override fun onStateChanged(bottomSheet: View, newState: Int) {
						if(newState == BottomSheetBehavior.STATE_EXPANDED) {
							lockBottomSheet()
							onExpandedListener?.onExpanded()
						}else if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
							unlockBottomSheet()
							onCollapsedListener?.onCollapsed()
						}
					}

					override fun onSlide(bottomSheet: View, slideOffset: Float) {}
				})

				it.state = if(isInitiallyExpanded) {
					(it as? LockableBottomSheetBehavior)?.isLocked = true
					BottomSheetBehavior.STATE_EXPANDED
				}else{
					BottomSheetBehavior.STATE_COLLAPSED
				}
			}
		}
	}

	fun lockBottomSheet() {
		(bottomSheetBehavior as? LockableBottomSheetBehavior)?.isLocked = true
	}

	fun unlockBottomSheet() {
		(bottomSheetBehavior as? LockableBottomSheetBehavior)?.isLocked = false
	}

	fun expandBottomSheet() {
		bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
	}

	fun collapseBottomSheet() {
		bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
	}

	fun setOnExpandedListener(listener: SheetExpandedListener) {
		onExpandedListener = listener
	}

	fun setOnCollapsedListener(listener: SheetCollapsedListener) {
		onCollapsedListener = listener
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		super.onLayout(changed, left, top, right, bottom)

		val behavior = bottomSheetBehavior ?: return

		val frontView = findViewById<BackdropFrontView>(R.id.layout_layer_front)
		frontView?.findViewById<View>(R.id.backdrop_content_divider)?.let { divider ->
			behavior.setPeekHeight(divider.top)
		}
	}

	fun interface SheetExpandedListener {
		fun onExpanded()
	}

	fun interface SheetCollapsedListener {
		fun onCollapsed()
	}

	class LockableBottomSheetBehavior<V: View>: BottomSheetBehavior<V> {
		var isLocked = false

		@Suppress("unused")
		constructor(): super()

		@Suppress("unused")
		constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

		override fun onInterceptTouchEvent(
			parent: CoordinatorLayout,
			child: V,
			event: MotionEvent
		): Boolean {
			return if(isLocked) false
				else super.onInterceptTouchEvent(parent, child, event)
		}

		override fun onTouchEvent(
			parent: CoordinatorLayout,
			child: V,
			event: MotionEvent
		): Boolean {
			return if(isLocked) false
				else super.onTouchEvent(parent, child, event)
		}

		override fun onStartNestedScroll(
			coordinatorLayout: CoordinatorLayout,
			child: V,
			directTargetChild: View,
			target: View,
			axes: Int,
			type: Int
		): Boolean {
			return if(isLocked) false
				else super.onStartNestedScroll(
					coordinatorLayout,
					child,
					directTargetChild,
					target,
					axes,
					type
				)
		}

		override fun onNestedPreScroll(
			coordinatorLayout: CoordinatorLayout,
			child: V,
			target: View,
			dx: Int,
			dy: Int,
			consumed: IntArray,
			type: Int
		) {
			if(!isLocked) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
		}

		override fun onStopNestedScroll(
			coordinatorLayout: CoordinatorLayout,
			child: V,
			target: View,
			type: Int
		) {
			if(!isLocked) super.onStopNestedScroll(coordinatorLayout, child, target, type)
		}

		override fun onNestedFling(
			coordinatorLayout: CoordinatorLayout,
			child: V,
			target: View,
			velocityX: Float,
			velocityY: Float,
			consumed: Boolean
		): Boolean {
			return if(isLocked) false
				else super.onNestedFling(
					coordinatorLayout,
					child,
					target,
					velocityX,
					velocityY,
					consumed
				)
		}
	}
}
