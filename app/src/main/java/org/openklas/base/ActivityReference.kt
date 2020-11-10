package org.openklas.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

object ActivityReference {
    private var mTopActivityWeakRef: WeakReference<Activity>? = null
    private val mActivityList: LinkedList<Activity> = LinkedList()
    private var mApplicationWeakRef: WeakReference<Application>? = null

    private val mCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            mActivityList.add(activity)
            setTopActivityWeakRef(
                activity
            )
        }

        override fun onActivityStarted(activity: Activity) {
            setTopActivityWeakRef(
                activity
            )
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivityWeakRef(
                activity
            )
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}
    }

    @JvmStatic
    fun initialize(application: Application) {
        mApplicationWeakRef = WeakReference(application)
        application.registerActivityLifecycleCallbacks(mCallbacks)
    }

    @JvmStatic
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("use NonActivityInterface.requireActivity() instead, don't use this methods in child project")
    fun getActivtyReference(): Activity? {
        return getActivity()
    }

    @JvmStatic
    fun getContext(): Context {
        return getActivity()
                ?: mApplicationWeakRef?.get() as Context
    }

    @JvmStatic
    fun getActivityList() =
        mActivityList

    @JvmStatic
    internal fun getActivity(): Activity? {
        if (mTopActivityWeakRef != null) {
            val activity = (mTopActivityWeakRef as WeakReference<Activity>).get()
            if (activity != null) {
                return activity.activityCheck()
            }
        }

        val size = mActivityList.size
        return if (size > 0) mActivityList[size - 1].activityCheck() else null
    }

    private fun Activity.activityCheck(): Activity? {
        if (this.isDestroyed) return null
        if (this.isFinishing) return null

        return this
    }

    private fun setTopActivityWeakRef(activity: Activity) {
        if (mTopActivityWeakRef == null || activity != (mTopActivityWeakRef as WeakReference<Activity>).get()) {
            mTopActivityWeakRef = WeakReference(activity)
        }
    }
}