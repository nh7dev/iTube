package dev.nh7.itube;

import android.app.Application
import dev.nh7.itube.browser.Browser
import dev.nh7.itube.utils.LOG

class MainApplication : Application() {

    var browser: Browser? = null

    override fun onCreate() {
        super.onCreate()
        LOG("onCreate Application")
    }
}
