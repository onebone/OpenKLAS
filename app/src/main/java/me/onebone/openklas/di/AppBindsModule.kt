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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.onebone.openklas.base.DefaultSemesterViewModelDelegate
import me.onebone.openklas.base.DefaultSessionViewModelDelegate
import me.onebone.openklas.base.SemesterViewModelDelegate
import me.onebone.openklas.base.SessionViewModelDelegate
import me.onebone.openklas.data.AccountDataSource
import me.onebone.openklas.data.DefaultAccountDataSource
import me.onebone.openklas.data.PreferenceCredentialDataSource
import me.onebone.openklas.data.KlasDataSource
import me.onebone.openklas.data.CredentialDataSource
import me.onebone.openklas.data.RemoteKlasDataSource
import me.onebone.openklas.data.RemoteSessionDataSource
import me.onebone.openklas.data.SessionDataSource
import me.onebone.openklas.repository.DefaultKlasRepository
import me.onebone.openklas.repository.DefaultSessionRepository
import me.onebone.openklas.repository.KlasRepository
import me.onebone.openklas.repository.SessionRepository
import javax.inject.Singleton
import me.onebone.openklas.base.ErrorViewModelDelegate
import me.onebone.openklas.base.ErrorViewModelDelegateImpl
import me.onebone.openklas.base.FlowRegistrar
import me.onebone.openklas.base.FlowRegistrarImpl

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

	@Binds
	@Singleton
	abstract fun bindErrorViewModelDelegate(impl: ErrorViewModelDelegateImpl): ErrorViewModelDelegate

	@Binds
	abstract fun bindFlowRegistrar(impl: FlowRegistrarImpl<Any>): FlowRegistrar<Any>
}
