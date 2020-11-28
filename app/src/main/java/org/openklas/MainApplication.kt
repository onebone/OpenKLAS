package org.openklas

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

import android.app.Activity
import android.app.Service
import androidx.fragment.app.Fragment
import com.github.windsekirun.daggerautoinject.DaggerAutoInject
import com.github.windsekirun.daggerautoinject.InjectApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import org.openklas.base.BaseApplication
import org.openklas.di.AppComponent
import org.openklas.di.DaggerAppComponent
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser
import javax.inject.Inject

@AttributeParser("org.openklas")
@InjectApplication(component = AppComponent::class)
class MainApplication : BaseApplication(),
	HasActivityInjector, HasServiceInjector,
	HasSupportFragmentInjector {

	@Inject
	lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

	@Inject
	lateinit var mServiceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

	@Inject
	lateinit var mFragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

	override val configFilePath: String
		get() = "config.json"

	override fun onCreate() {
		super.onCreate()

		mAppComponent = DaggerAppComponent.builder()
			.application(this)
			.build()
		DaggerAutoInject.init(this, mAppComponent)
	}

	override fun activityInjector(): DispatchingAndroidInjector<Activity> {
		return mActivityDispatchingAndroidInjector
	}

	override fun serviceInjector(): AndroidInjector<Service> {
		return mServiceDispatchingAndroidInjector
	}

	override fun supportFragmentInjector(): AndroidInjector<Fragment> {
		return mFragmentDispatchingAndroidInjector
	}

	companion object {
		private var mAppComponent: AppComponent? = null

		val appComponent: AppComponent?
			get() = mAppComponent
	}
}
