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

package me.onebone.openklas.di

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import me.onebone.openklas.BuildConfig
import me.onebone.openklas.base.Config
import me.onebone.openklas.base.DefaultSubjectViewModelDelegate
import me.onebone.openklas.base.SemesterViewModelDelegate
import me.onebone.openklas.base.SubjectViewModelDelegate
import me.onebone.openklas.klas.DefaultKlasClient
import me.onebone.openklas.klas.KlasClient
import me.onebone.openklas.klas.KlasUri
import me.onebone.openklas.klas.service.KlasService
import me.onebone.openklas.klas.test.DemoKlasClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class AppProvidesModule {
	@Singleton
	@Provides
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		json: Json
	): Retrofit {
		val builder = Retrofit.Builder()
			.baseUrl(KlasUri.ROOT_URI)
			.client(okHttpClient)

		@OptIn(ExperimentalSerializationApi::class)
		builder.addConverterFactory(json.asConverterFactory("application/json".toMediaType()))

		return builder.build()
	}

	@Singleton
	@Provides
	fun provideJson(): Json {
		return Json {
			ignoreUnknownKeys = true
			encodeDefaults = true
		}
	}

	@Singleton
	@Provides
	fun provideClient(
		cookieJar: CookieJar
	): OkHttpClient {
		val builder = OkHttpClient().newBuilder()

		if(BuildConfig.DEBUG) {
			builder.addInterceptor(HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			})
		}

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
	fun provideKlasClient(service: KlasService, json: Json): KlasClient {
		return if(BuildConfig.FETCH_ONLINE) DefaultKlasClient(service, json)
			else DemoKlasClient()
	}

	@Provides
	@Singleton
	fun provideSubjectViewModelDelegate(semesterViewModelDelegate: SemesterViewModelDelegate): SubjectViewModelDelegate {
		return DefaultSubjectViewModelDelegate(semesterViewModelDelegate)
	}
}
