package org.openklas.data

import io.reactivex.Single
import org.openklas.klas.KlasClient
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester
import javax.inject.Inject

class RemoteKlasDataSource @Inject constructor(
	private val klas: KlasClient
): KlasDataSource {
	override fun performLogin(username: String, password: String): Single<String> {
		return klas.login(username, password)
	}

	override fun getHome(semester: String): Single<Home> {
		return klas.getHome(semester)
	}

	override fun getSemesters(): Single<Array<Semester>> {
		return klas.getSemesters()
	}
}
