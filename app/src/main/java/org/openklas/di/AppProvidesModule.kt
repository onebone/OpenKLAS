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
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.openklas.base.Config
import org.openklas.klas.KlasUri
import org.openklas.klas.deserializer.TypeResolvableJsonDeserializer
import org.openklas.klas.service.KlasService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppProvidesModule {
	@Provides
	fun provideGson(
		deserializers: Set<@JvmSuppressWildcards TypeResolvableJsonDeserializer<*>>
	): Gson {
		val builder = GsonBuilder().serializeSpecialFloatingPointValues()
			.serializeNulls()

		deserializers.forEach {
			builder.registerTypeAdapter(it.getType(), it)
		}

		return builder.create()
	}

	@Provides
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		gson: Gson
	): Retrofit {
		val builder = Retrofit.Builder()
			.baseUrl(KlasUri.ROOT_URI)
			.client(okHttpClient)

		builder.addConverterFactory(GsonConverterFactory.create(gson))

		if (!Config.config.disableRxAdapter) {
			builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		}

		return builder.build()
	}

	@Provides
	fun provideClient(
		interceptors: Set<@JvmSuppressWildcards Interceptor>,
		@Named("loginterceptor") logsInterceptor: Interceptor,
		cookieJar: CookieJar
	): OkHttpClient {
		val builder = OkHttpClient().newBuilder()
		builder.readTimeout(Config.config.timeout.toLong(), TimeUnit.MILLISECONDS)
		builder.connectTimeout(Config.config.connectTimeout.toLong(), TimeUnit.MILLISECONDS)
		builder.retryOnConnectionFailure(Config.config.retryOnConnectionFailure)
		builder.cookieJar(cookieJar)
		builder.followRedirects(false)

		if (interceptors.isNotEmpty()) {
			interceptors.forEach {
				builder.addInterceptor(it)
			}
		}

		if (!Config.config.notUseLogInterceptor) {
			builder.addInterceptor(logsInterceptor)
		}

		return builder.build()
	}

	@Provides
	@Singleton
	fun provideKlasService(retrofit: Retrofit): KlasService {
		return retrofit.create(KlasService::class.java)
	}

	@Provides
	@Singleton
	fun provideCookieJar(app: Application): CookieJar {
		return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(app))
	}
}
