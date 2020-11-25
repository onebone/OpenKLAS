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

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.openklas.base.impl.BaseInterface
import org.openklas.base.impl.NonActivityInterface
import org.openklas.utils.weak


abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
	BaseInterface,
	DefaultLifecycleObserver, Observable, NonActivityInterface {
	private var mCallbacks: PropertyChangeRegistry? = null
	protected val compositeDisposable = CompositeDisposable()

	var lifecycle: Lifecycle? by weak(null)

	override fun onDestroy(owner: LifecycleOwner) {
		super.onDestroy(owner)
		compositeDisposable.clear()
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	protected fun postEvent(event: Any) {
		EventBus.getDefault().post(event)
	}

	protected fun addDisposable(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}

	protected fun removeDisposable(disposable: Disposable) {
		compositeDisposable.delete(disposable)
	}


	override fun addOnPropertyChangedCallback(onPropertyChangedCallback: Observable.OnPropertyChangedCallback) {
		if (mCallbacks == null) {
			mCallbacks = PropertyChangeRegistry()
		}

		mCallbacks?.add(onPropertyChangedCallback)
	}

	override fun removeOnPropertyChangedCallback(onPropertyChangedCallback: Observable.OnPropertyChangedCallback) {
		if (mCallbacks == null) {
			mCallbacks = PropertyChangeRegistry()
		}

		mCallbacks?.remove(onPropertyChangedCallback)
	}

	fun notifyChange() {
		synchronized(this) {
			if (mCallbacks == null) {
				return
			}
		}
		mCallbacks?.notifyCallbacks(this, 0, null)
	}

	fun notifyPropertyChanged(fieldId: Int) {
		synchronized(this) {
			if (mCallbacks == null) {
				return
			}
		}
		mCallbacks?.notifyCallbacks(this, fieldId, null)
	}
}
