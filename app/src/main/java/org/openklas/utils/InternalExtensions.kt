package org.openklas.utils

import android.content.Context
import org.openklas.base.ActivityReference
import pyxis.uzuki.live.nyancat.NyanCat

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