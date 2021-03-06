package net.aquadc.flawless.androidView.util

import net.aquadc.flawless.screen.VisibilityState
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.screen.VisibilityStateListener

internal class VisibilityStateListeners {

    private val visibilityStateListeners = ArrayList<VisibilityStateListener>()

    fun updated(host: Host, old: VisibilityState, new: VisibilityState) {
        if (old == new) return
        visibilityStateListeners.toTypedArray().forEach { it.onVisibilityStateChanged(host, old, new) }
        // yea, it's defensive copying, so listener can remove itself during iteration
    }

    fun add(listener: VisibilityStateListener) {
        visibilityStateListeners.add(listener)
    }

    fun remove(listener: VisibilityStateListener) {
        visibilityStateListeners.remove(listener)
    }

}
