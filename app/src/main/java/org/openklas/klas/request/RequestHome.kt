package org.openklas.klas.request

import com.google.gson.annotations.SerializedName

data class RequestHome (
	@SerializedName("searchYearhakgi")
	val semester: String
)
