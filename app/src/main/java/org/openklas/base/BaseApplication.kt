package org.openklas.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.bumptech.glide.BuildConfig
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser
import pyxis.uzuki.live.nyancat.NyanCatGlobal
import pyxis.uzuki.live.nyancat.config.LoggerConfig
import pyxis.uzuki.live.nyancat.config.TriggerTiming

/**
 * OpenKlas
 * Class: BaseApplication
 * Created by limmoong on 2020/11/10.
 *
 * Description:
 */
@AttributeParser("org.openklas.base")
abstract class BaseApplication : Application() {
    abstract val configFilePath: String

    override fun onCreate() {
        super.onCreate()
        Config.loadConfig(this, configFilePath)

        val config = LoggerConfig(
            packageName, BuildConfig.DEBUG,
            if (Config.config.printLogRelease) TriggerTiming.ALL else TriggerTiming.ONLY_DEBUG
        )
        NyanCatGlobal.breed(config)

        ActivityReference.initialize(this)
        pyxis.uzuki.live.richutilskt.module.reference.ActivityReference.initialize(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}