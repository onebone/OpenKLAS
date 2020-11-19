package org.openklas.data

import io.reactivex.Single
import org.openklas.klas.model.Home
import org.openklas.klas.model.Semester

interface KlasDataSource {
	fun performLogin(username: String, password: String): Single<String>
	fun getHome(semester: String): Single<Home>
	fun getSemesters(): Single<Array<Semester>>
}
