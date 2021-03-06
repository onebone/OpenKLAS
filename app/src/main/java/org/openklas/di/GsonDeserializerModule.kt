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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import org.openklas.klas.deserializer.CreditStatusDeserializer
import org.openklas.klas.deserializer.LectureScheduleDeserializer
import org.openklas.klas.deserializer.OnlineContentEntryDeserializer
import org.openklas.klas.deserializer.SyllabusDeserializer
import org.openklas.klas.deserializer.TypeResolvableJsonDeserializer
import org.openklas.klas.deserializer.TimetableDeserializer
import javax.inject.Singleton
import org.openklas.klas.deserializer.LocalDateDeserializer
import org.openklas.klas.deserializer.ZonedDateTimeDeserializer

@Module
@InstallIn(SingletonComponent::class)
class GsonDeserializerModule {
	@Provides
	@IntoSet
	@Singleton
	fun provideTimetableDeserializer(): TypeResolvableJsonDeserializer<*> {
		return TimetableDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideOnlineLectureEntryDeserializer(): TypeResolvableJsonDeserializer<*> {
		return OnlineContentEntryDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideSyllabusDeserializer(): TypeResolvableJsonDeserializer<*> {
		return SyllabusDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideLectureScheduleDeserializer(): TypeResolvableJsonDeserializer<*> {
		return LectureScheduleDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideCreditStatusDeserializer(): TypeResolvableJsonDeserializer<*> {
		return CreditStatusDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideZonedDateTimeDeserializer(): TypeResolvableJsonDeserializer<*> {
		return ZonedDateTimeDeserializer()
	}

	@Provides
	@IntoSet
	@Singleton
	fun provideLocalDateDeserializer(): TypeResolvableJsonDeserializer<*> {
		return LocalDateDeserializer()
	}
}
