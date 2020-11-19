package org.openklas.data

interface AccountDataSource {
	fun getAccount(): Account?

	data class Account(
		val username: String,
		val password: String
	)
}
