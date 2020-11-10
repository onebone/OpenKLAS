package org.openklas.base

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.openklas.utils.fileAsString

object Config {
    @JvmStatic
    var config = ConfigItem()

    /**
     * load Settings from given [path]
     * @param context Context
     * @param path String
     */
    fun loadConfig(context: Context, path: String) {
        val configItem: ConfigItem
        val jsonStr = context.assets.fileAsString(path)

        configItem = if (TextUtils.isEmpty(jsonStr)) {
            ConfigItem()
        } else {
            Gson().fromJson(jsonStr, ConfigItem::class.java)
        }

        config = configItem
    }

    class ConfigItem {
        /**
         * set timeout of connections
         * default is 10000ms (10s)
         */
        @SerializedName("ConnectTimeout")
        val connectTimeout = 10000

        /**
         * Set flags of display logs in release mode.
         * caution, this options will work in NyanCat logger, not standard logger.
         */
        @SerializedName("PrintingLog")
        val printLogRelease = true

        /**
         * Set flags of display details log for networking connections.
         * if true, it will use ihsanbal/LoggingInterceptor
         * otherwise, it will use okhttp3/logging-interceptor
         */
        @SerializedName("ProvideDetailLog")
        val provideDetailsLog = false

        /**
         * Set url of networking connections.
         * it must be end with '/'
         * form: http://127.0.0.1:8080
         */
        @SerializedName("ServerUrl")
        val serverUrl = ""

        /**
         * Set timeout of network request.
         * detault is 20000ms (20s)
         */
        @SerializedName("Timeout")
        val timeout = 20000

        /**
         * Set flags of require portraint only
         */
        @SerializedName("RequirePortrait")
        val requirePortrait = true

        /**
         * Set flags of enable RxJava-Adapter in Retrofit
         */
        @SerializedName("DisableRxAdapter")
        val disableRxAdapter = false

        /**
         * Set flags of register Eventbus event in onCreate() and unregister in onDestroy()
         * if true, Eventbus will register in onCreate() and un-register in onDestroy()
         * otherwise, Eventbus will register in onStart() and un-register in onStop()
         */
        @SerializedName("RegisterEventBusInOnCreate")
        val registerBusOnCreate: Boolean = false

        /**
         * Set flags on enable retryOnConnectionFailure in OKHttp
         * default is true
         */
        @SerializedName("RetryOnConnectionFailure")
        val retryOnConnectionFailure: Boolean = true

        /**
         * Set flags on disable LogInterceptor in OKHttp
         * default is false
         */
        @SerializedName("NotUseLogInterceptor")
        val notUseLogInterceptor: Boolean = false

        /**
         * Set threshold of Binary omitted size.
         * It may helpful for large-body size
         *
         * default is 10000
         */
        @SerializedName("BinaryThresholdLogInterceptor")
        val binaryThreshold: Int = 10000

        /**
         * Using Jackson as Retrofit converter instead of GSON.
         *
         * default is false
         */
        @SerializedName("UseJacksonAsConverter")
        val useJacksonAsConverter: Boolean = false

        /**
         * Set port number that using in WatchTower
         * it will change port of WebServer.
         *
         * default is 8085
         */
        @SerializedName("WatchTowerPort")
        val watchTowerPort: Int = 8085
    }
}
