package com.abcode.quran.audio

import android.content.Context

/**
 * Holds the Android Application context so shared (common) code can create
 * platform components that require it. Must be initialized from the Android app.
 */
object AppContext {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun requireContext(): Context =
        appContext ?: error("AppContext.init(context) must be called before using audio/sharing on Android")
}
