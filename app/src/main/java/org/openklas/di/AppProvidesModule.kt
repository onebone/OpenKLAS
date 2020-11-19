package org.openklas.di

import android.app.Application
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.openklas.MainApplication
import org.openklas.base.Config
import org.openklas.klas.deserializer.TypeResolvableJsonDeserializer
import org.openklas.klas.service.KlasService
import org.openklas.net.RWJacksonConfig
import pyxis.uzuki.live.richutilskt.utils.RPreference
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppProvidesModule {
	@Provides
	@Singleton
	fun provideRPerference(application: MainApplication?): RPreference {
		return RPreference.getInstance(application!!)
	}

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
	fun provideObjectMapper(configs: Set<@JvmSuppressWildcards RWJacksonConfig>): ObjectMapper {
		val mapperFeatureMap: MutableMap<MapperFeature, Boolean> = mutableMapOf()
		val serializationFeatureMap: MutableMap<SerializationFeature, Boolean> = mutableMapOf()
		val deserializationFeatureMap: MutableMap<DeserializationFeature, Boolean> = mutableMapOf()
		val jsonParserMap: MutableMap<JsonParser.Feature, Boolean> = mutableMapOf()

		mapperFeatureMap.putAll(configs.map { it.getMapperFeaturePairs() }.flatten())
		serializationFeatureMap.putAll(configs.map { it.getSerialzationFeaturePairs() }.flatten())
		deserializationFeatureMap.putAll(configs.map { it.getDeserialzationFeaturePairs() }
			.flatten())
		jsonParserMap.putAll(configs.map { it.getJsonParserPairs() }.flatten())

		return ObjectMapper().apply {
			for (feature in mapperFeatureMap) configure(feature.key, feature.value)
			for (feature in serializationFeatureMap) configure(feature.key, feature.value)
			for (feature in deserializationFeatureMap) configure(feature.key, feature.value)
			for (feature in jsonParserMap) configure(feature.key, feature.value)
		}
	}

	@Provides
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		gson: Gson,
		objectMapper: ObjectMapper
	): Retrofit {
		val builder = Retrofit.Builder()
			.baseUrl(Config.config.serverUrl)
			.client(okHttpClient)

		if (Config.config.useJacksonAsConverter) {
			builder.addConverterFactory(JacksonConverterFactory.create(objectMapper))
		} else {
			builder.addConverterFactory(GsonConverterFactory.create(gson))
		}

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
