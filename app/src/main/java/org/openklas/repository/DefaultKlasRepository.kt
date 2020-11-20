package org.openklas.repository

import io.reactivex.Single
import org.openklas.data.KlasDataSource
import org.openklas.data.PreferenceDataSource
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import javax.inject.Inject

class DefaultKlasRepository @Inject constructor(
	private val klasDataSource: KlasDataSource,
	private val preferenceDataSource: PreferenceDataSource
): KlasRepository {
	override fun performLogin(username: String, password: String): Single<String> {
		return klasDataSource.performLogin(username, password)
			.doOnSuccess {
				preferenceDataSource.userID = username
				preferenceDataSource.password = password
			}
	}

	override fun getHome(semester: String): Single<Home> {
		return klasDataSource.getHome(semester)
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return klasDataSource.getSemesters()
	}
}
