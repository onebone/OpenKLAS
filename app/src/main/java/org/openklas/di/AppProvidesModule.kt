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

package org.openklas.di

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import org.openklas.BuildConfig
import org.openklas.base.Config
import org.openklas.base.DefaultSubjectViewModelDelegate
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SubjectViewModelDelegate
import org.openklas.klas.DefaultKlasClient
import org.openklas.klas.KlasClient
import org.openklas.klas.KlasUri
import org.openklas.klas.deserializer.TypeResolvableJsonDeserializer
import org.openklas.klas.service.KlasService
import org.openklas.klas.test.DemoKlasClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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

		return builder.build()
	}

	@Provides
	fun provideClient(
		cookieJar: CookieJar
	): OkHttpClient {
		val builder = OkHttpClient().newBuilder()
		builder.readTimeout(Config.config.timeout.toLong(), TimeUnit.MILLISECONDS)
		builder.connectTimeout(Config.config.connectTimeout.toLong(), TimeUnit.MILLISECONDS)
		builder.retryOnConnectionFailure(Config.config.retryOnConnectionFailure)
		builder.cookieJar(cookieJar)
		builder.followRedirects(false)

		return builder.build()
	}

	@Provides
	@Singleton
	fun provideKlasService(retrofit: Retrofit): KlasService {
		return retrofit.create(KlasService::class.java)
	}

	@Provides
	@Singleton
	fun provideCookieJar(@ApplicationContext context: Context): CookieJar {
		return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
	}

	@Provides
	@Singleton
	fun provideKlasClient(service: KlasService, gson: Gson): KlasClient {
		return if(BuildConfig.FETCH_ONLINE) DefaultKlasClient(service, gson)
			else DemoKlasClient()
	}

	@Provides
	@Singleton
	fun provideSubjectViewModelDelegate(semesterViewModelDelegate: SemesterViewModelDelegate): SubjectViewModelDelegate {
		return DefaultSubjectViewModelDelegate(semesterViewModelDelegate)
	}
}
