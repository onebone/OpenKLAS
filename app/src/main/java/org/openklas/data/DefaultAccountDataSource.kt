package org.openklas.data

import javax.inject.Inject

class DefaultAccountDataSource @Inject constructor(
	private val preferenceRepository: PreferenceDataSource
): AccountDataSource {
	override fun getAccount(): AccountDataSource.Account? {
		val userId = preferenceRepository.userID
		val password = preferenceRepository.password

		return if(userId != null && password != null) AccountDataSource.Account(userId, password)
			else null
	}
}
