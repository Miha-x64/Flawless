package net.aquadc.flawless.implementMe

import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.Host

interface VisibilityStateListener {
    fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState)
}
