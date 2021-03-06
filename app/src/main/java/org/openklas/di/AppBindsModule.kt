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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.openklas.base.DefaultSemesterViewModelDelegate
import org.openklas.base.DefaultSessionViewModelDelegate
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.base.SessionViewModelDelegate
import org.openklas.data.AccountDataSource
import org.openklas.data.DefaultAccountDataSource
import org.openklas.data.PreferenceCredentialDataSource
import org.openklas.data.KlasDataSource
import org.openklas.data.CredentialDataSource
import org.openklas.data.RemoteKlasDataSource
import org.openklas.data.RemoteSessionDataSource
import org.openklas.data.SessionDataSource
import org.openklas.repository.DefaultKlasRepository
import org.openklas.repository.DefaultSessionRepository
import org.openklas.repository.KlasRepository
import org.openklas.repository.SessionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModule {
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
	abstract fun bindCredentialDataSource(impl: PreferenceCredentialDataSource): CredentialDataSource

	@Binds
	@Singleton
	abstract fun bindAccountDataSource(dataSource: DefaultAccountDataSource): AccountDataSource

	@Binds
	@Singleton
	abstract fun bindSessionViewModelDelegate(sessionViewModelDelegate: DefaultSessionViewModelDelegate): SessionViewModelDelegate

	@Binds
	@Singleton
	abstract fun bindSemesterViewModelDelegate(semesterViewModelDelegate: DefaultSemesterViewModelDelegate): SemesterViewModelDelegate
}
