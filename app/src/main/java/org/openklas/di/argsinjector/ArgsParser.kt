package com.github.windsekirun.baseapp.module.argsinjector

import android.app.Activity
import android.os.Bundle
import pyxis.uzuki.live.richutilskt.utils.tryCatch
import java.lang.reflect.Field

/**
 * PyxisBaseApp
 * Class: ArgsParser
 * Created by Pyxis on 2/4/18.
 *
 * Description:
 */

object ArgsParser {

    @JvmStatic
    @JvmOverloads
    fun inject(receiver: Any, bundle: Bundle? = null) {
        val newBundle: Bundle? = when (receiver) {
            is Activity -> receiver.intent?.extras
            is SupportFragment -> receiver.arguments
            else -> bundle
        }

        tryCatch {
            var cls: Class<Any>? = receiver.javaClass

            do {
                cls?.declaredFields?.filter { it.declaredAnnotations.isNotEmpty() }?.forEach { field ->
                    field.declaredAnnotations.forEach {
                        when (it) {
                            is Extra -> attachExtra(it, field, receiver, newBundle)
                            is Argument -> attachArgument(it, field, receiver, newBundle)
                        }
                    }
                }

                cls = try {
                    cls?.getSuperclass()
                } catch (e: Exception) {
                    null
                }
            } while (cls != null)
        }
    }

    private fun attachExtra(extra: Extra, field: Field, receiver: Any, bundle: Bundle?) {
        var name = extra.value
        if (name.isEmpty()) {
            name = field.name
        }

        val value = bundle?.get(name)

        if (value != null) {
            field.isAccessible = true
            field.set(receiver, value)
        }
    }

    private fun attachArgument(argument: Argument, field: Field, receiver: Any, bundle: Bundle?) {
        var name = argument.value
        if (name.isEmpty()) {
            name = field.name
        }

        val value = bundle?.get(name)

        if (value != null) {
            field.isAccessible = true
            field.set(receiver, value)
        }
    }
}
