package net.aquadc.flawless.implementMe

import android.support.v4.app.Fragment
import net.aquadc.flawless.VisibilityState

interface VisibilityStateListener {
    fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState)
}
