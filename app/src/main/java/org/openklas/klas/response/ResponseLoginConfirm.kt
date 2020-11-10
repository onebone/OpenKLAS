package org.openklas.klas.response

import com.google.gson.annotations.SerializedName

data class ResponseLoginConfirm (
	val errorCount: Int,
	// fieldErrors: Array<?>
	val loginRequired: Boolean,
	val redirect: Boolean,
	val redirectUrl: String,
	@SerializedName("response")
	val response: ResponseLoginConfirmData,
	val responseText: String
)

data class ResponseLoginConfirmData (
	// frstPwdAt?
	// pushToken?
	val userId: String
)
