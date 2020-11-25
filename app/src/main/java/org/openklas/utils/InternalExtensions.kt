package org.openklas.utils

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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

import android.content.Context
import pyxis.uzuki.live.nyancat.NyanCat
import pyxis.uzuki.live.richutilskt.module.reference.ActivityReference

inline fun catchAll(action: () -> Unit) {
	try {
		action()
	} catch (t: Throwable) {
		NyanCat.tag("exception").e("Catch an exception. ${t.message}", t)
	}
}

fun convertToList(vararg resources: Int): List<String> {
	val context = ActivityReference.getContext()
	return resources.map { context.getString(it) }
}

fun convertToArray(vararg resources: Int): Array<String> {
	val context = ActivityReference.getContext()
	return resources.map { context.getString(it) }.toTypedArray()
}

fun <T> convertToResourceList(vararg resources: Int, action: Function2<Context, Int, T>): List<T> {
	val context = ActivityReference.getContext()
	return resources.map { action(context, it) }
}

fun <T> convertArrayToList(vararg resources: Int): List<Int> {
	return resources.toList()
}
