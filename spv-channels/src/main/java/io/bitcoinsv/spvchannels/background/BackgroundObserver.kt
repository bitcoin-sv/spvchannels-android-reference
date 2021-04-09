
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.background

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.lang.ref.WeakReference

/**
 * Used to monitor enter background and exit background events
 */
internal class BackgroundObserver(context: Context) : ComponentCallbacks2 {
    private val listeners = mutableListOf<WeakReference<() -> Unit>>()

    init {
        context.applicationContext.registerComponentCallbacks(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onLowMemory() {
    }

    /**
     * If TRIM_MEMORY_UI_HIDDEN is sent, that means that the app UI is no longer visible, ergo
     * the app entered background.
     */
    override fun onTrimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d("BG", "Entered background")
            listeners.removeAll { it.get() == null }
            listeners.forEach { it.get()?.invoke() }
        }
    }

    /**
     * Captures a weak reference to the callback. The callback will be invoked when the app
     * enters background.
     */
    fun addOnEnterBackgroundCallback(listener: () -> Unit) {
        listeners.add(WeakReference(listener))
    }
}
