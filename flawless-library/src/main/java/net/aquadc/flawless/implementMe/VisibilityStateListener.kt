package net.aquadc.flawless.implementMe

import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.Host


interface VisibilityStateListener {
    fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState)
}


inline fun VisibilityStateListener(
        crossinline func: (host: Host<*>, old: VisibilityState, new: VisibilityState) -> Unit
) = object  : VisibilityStateListener {
    override fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState) =
            func(host, old, new)
}

inline fun ViewFirstShownListener(
        crossinline func: (host: Host<*>) -> Unit
) = object : VisibilityStateListener {
    var called = false
    override fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState) {
        if (new == VisibilityState.Visible && !called) {
            func(host)
            called = true
        } else if (new == VisibilityState.Uninitialized) {
            called = false
        }
    }
}
