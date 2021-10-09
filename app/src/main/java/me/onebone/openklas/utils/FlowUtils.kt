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

package me.onebone.openklas.utils

import kotlin.experimental.ExperimentalTypeInference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform

@OptIn(ExperimentalTypeInference::class)
inline fun <T> sharedFlow(
	retryTrigger: Channel<Unit>,
	scope: CoroutineScope,
	@BuilderInference crossinline block: suspend FlowCollector<T>.() -> Unit
): Flow<T> = flow {
	while(true) {
		block()

		val result = retryTrigger.receiveCatching()
		if(result.isClosed) break
	}
}.shareIn(scope, SharingStarted.WhileSubscribed(), replay = 1)

@Suppress("FunctionName")
fun <T> SharedEventFlow(): MutableSharedFlow<T> = MutableSharedFlow(
	replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
)

inline fun <T, R> Flow<ViewResource<T>>.mapResource(
	crossinline block: suspend (T) -> R
): Flow<ViewResource<R>> = map {
	when(it) {
		is ViewResource.Success -> {
			try {
				ViewResource.Success(block(it.value))
			}catch(e: Throwable) {
				ViewResource.Error(e)
			}
		}
		is ViewResource.Error -> ViewResource.Error(it.error)
		is ViewResource.Loading -> ViewResource.Loading()
	}
}

inline fun <T, R> Flow<ViewResource<T>>.flatMapResource(
	crossinline block: suspend (T) -> ViewResource<R>
): Flow<ViewResource<R>> = transform {
	emit(when(it) {
		is ViewResource.Success -> block(it.value)
		is ViewResource.Error -> ViewResource.Error(it.error)
		is ViewResource.Loading -> ViewResource.Loading()
	})
}

/**
 * Emits loading state prior to processing the stream. Following operators
 * will use `mapResource` or `flatMapResource` to transform succeed value.
 *
 * ```
 * intFlow.loadOnEach()
 *     .mapResource {
 *         it + 10
 *     }
 * ```
 */
fun <T> Flow<T>.loadOnEach(): Flow<ViewResource<T>> = transform { value ->
	emit(ViewResource.Loading())
	emit(ViewResource.Success(value))
}
