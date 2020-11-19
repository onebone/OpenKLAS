package org.openklas.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
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
