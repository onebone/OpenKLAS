package org.openklas.klas

sealed class Result<T>

data class Success<T> (
	val data: T
): Result<T>()

data class Failure<T> (
	val reason: Throwable
): Result<T>()
