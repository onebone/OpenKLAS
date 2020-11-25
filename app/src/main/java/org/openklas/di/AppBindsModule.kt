package org.openklas.di

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
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor
import org.openklas.MainApplication
import org.openklas.base.DefaultSessionViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.data.AccountDataSource
import org.openklas.data.DefaultAccountDataSource
import org.openklas.data.DefaultPreferenceDataSource
import org.openklas.data.KlasDataSource
import org.openklas.data.PreferenceDataSource
import org.openklas.data.RemoteKlasDataSource
import org.openklas.data.RemoteSessionDataSource
import org.openklas.data.SessionDataSource
import org.openklas.di.factory.DaggerViewModelFactory
import org.openklas.net.interceptor.LogInterceptor
import org.openklas.repository.DefaultKlasRepository
import org.openklas.repository.DefaultSessionRepository
import org.openklas.repository.KlasRepository
import org.openklas.repository.SessionRepository
import javax.inject.Named
import javax.inject.Singleton

/**
 * OpenKlas
 * Class: AppBindsModule
 * Created by limmoong on 2020/11/10.
 *
 *
 * Description:
 */
@Module
abstract class AppBindsModule {
	@Binds
	abstract fun bindApplication(application: MainApplication?): Application?

	@Binds
	abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

	@Binds
	@Named("loginterceptor")
	abstract fun bindLogInterceptor(interceptor: LogInterceptor): Interceptor

	@Binds
	@Singleton
	abstract fun bindKlasRepository(klasRepository: DefaultKlasRepository): KlasRepository

	@Binds
	@Singleton
	abstract fun bindKlasDataSource(dataSource: RemoteKlasDataSource): KlasDataSource

	@Binds
	@Singleton
	abstract fun bindSessionDataSource(dataSource: RemoteSessionDataSource): SessionDataSource

	@Binds
	@Singleton
	abstract fun bindSessionRepository(repository: DefaultSessionRepository): SessionRepository

	@Binds
	@Singleton
	abstract fun bindPreferenceDataSource(impl: DefaultPreferenceDataSource): PreferenceDataSource

	@Binds
	@Singleton
	abstract fun bindAccountDataSource(dataSource: DefaultAccountDataSource): AccountDataSource

	@Binds
	@Singleton
	abstract fun bindSessionViewModelDelegate(sessionViewModelDelegate: DefaultSessionViewModelDelegate): SessionViewModelDelegate
}
