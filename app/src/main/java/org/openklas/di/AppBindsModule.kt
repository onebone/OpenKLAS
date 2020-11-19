package org.openklas.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor
import org.openklas.MainApplication
import org.openklas.data.KlasDataSource
import org.openklas.data.RemoteKlasDataSource
import org.openklas.di.factory.DaggerViewModelFactory
import org.openklas.net.interceptor.LogInterceptor
import org.openklas.repository.DefaultKlasRepository
import org.openklas.repository.KlasRepository
import javax.inject.Named

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
	abstract fun bindKlasRepository(klasRepository: DefaultKlasRepository): KlasRepository

	@Binds
	abstract fun bindKlasDataSource(dataSource: RemoteKlasDataSource): KlasDataSource
}
