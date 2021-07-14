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

package org.openklas.utils

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class FlowUtilsTest {
	@Test
	fun sharedFlowTest(): Unit = runBlocking {
		val trigger = Channel<Unit>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

		val collectScope = this + SupervisorJob()
		val flow = sharedFlow(trigger, collectScope) {
			emit(1)
			emit(2)
		}

		launch {
			repeat(2) { // retry 2 times
				delay(100L)
				trigger.send(Unit)
			}

			trigger.close()
		}

		withTimeout(500) {
			assertThat(
				flow.take(6).toList(),
				`is`(equalTo(listOf(1, 2, 1, 2, 1, 2)))
			)

			assertThat(
				flow.first(),
				`is`(equalTo(2))
			) // SharedFlow yields last emitted value if replay=1
		}

		// no need to collect further... stop here
		collectScope.cancel()
	}
}
