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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn

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
