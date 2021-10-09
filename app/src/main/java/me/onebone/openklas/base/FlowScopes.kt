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

package me.onebone.openklas.base

import javax.inject.Inject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Used to control multiple flows to be managed by their own keys.
 * When you are using this in ViewModel, delegate implementation to
 * the injected object.
 * ```
 *     @HiltViewModel
 *     class MyViewModel @Inject constructor(
 *         registrar: FlowRegistrar<Any>
 *     ): ViewModel(), KeyedScope<MyKey> by registrar {
 *         val network = registrar.register(MyKey.Network) {
 *             flow {
 *                 emit(LoadState.Loading)
 *                 emit(getNetworkResponse())
 *             }
 *         }
 *
 *         ...
 *         cancel(MyKey.Network)
 *         restart(MyKey.Network)
 *     }
 * ```
 */
interface KeyedScope<in T> {
	fun cancel(key: T)
	fun restart(key: T)
}

interface FlowRegistrar<in T>: KeyedScope<T> {
	fun<R> register(key: T, factory: () -> Flow<R>): Flow<R>
}

class FlowRegistrarImpl<T> @Inject constructor(): FlowRegistrar<T> {
	private val map: MutableMap<T, Channel<Unit>> = mutableMapOf()

	@OptIn(ExperimentalCoroutinesApi::class)
	override fun<R> register(key: T, factory: () -> Flow<R>): Flow<R> {
		val channel = Channel<Unit>()

		synchronized(map) {
			map[key]?.close()
			map[key] = channel
		}

		return channelFlow {
			launch(start = CoroutineStart.UNDISPATCHED) {
				var previousJob: Job? = null

				while(true) {
					previousJob?.cancel()
					previousJob = launch(start = CoroutineStart.UNDISPATCHED) {
						factory().collect {
							send(it)
						}
					}

					val result = channel.receiveCatching()
					if(!result.isSuccess) break
				}

				previousJob?.cancel()
			}

			awaitClose {
				cancel(key)
			}
		}
	}

	override fun cancel(key: T) {
		val channel = synchronized(map) {
			val channel = map[key] ?: return
			map.remove(key)

			channel
		}

		channel.close()
	}

	override fun restart(key: T) {
		val channel = synchronized(map) {
			map[key]
		} ?: return

		channel.trySend(Unit)
	}
}
