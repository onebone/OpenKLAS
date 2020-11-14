package org.openklas.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import org.openklas.klas.deserializer.TypeResolvableJsonDeserializer
import org.openklas.klas.deserializer.TimetableDeserializer
import javax.inject.Singleton

@Module
class GsonDeserializerModule {
	@Provides
	@IntoSet
	@Singleton
	fun provideTimetableDeserializer(): TypeResolvableJsonDeserializer<*> {
		return TimetableDeserializer()
	}
}
