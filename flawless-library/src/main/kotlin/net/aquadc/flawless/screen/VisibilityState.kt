package net.aquadc.flawless.screen

import android.support.v4.app.Fragment
import net.aquadc.flawless.androidView.Host

/**
 * Represents View's visibility state.
 */
enum class VisibilityState {

    /**
     * The view was not created yet.
     */
    Uninitialized,

    /**
     * The view does already exist, but invisible for user,
     * e. g. because [Fragment.getUserVisibleHint] is false.
     */
    Invisible,

    /**
     * The view is visible.
     * For fragment this means [Fragment.getView] != null, [Fragment.isAdded], and [Fragment.getUserVisibleHint].
     */
    Visible

}

/**
 * Implementing class observes view visibility state changes.
 */
interface VisibilityStateListener {
    fun onVisibilityStateChanged(host: Host, old: VisibilityState, new: VisibilityState)
}

/**
 * SAM-converter for [VisibilityStateListener].
 */
inline fun VisibilityStateListener(
        crossinline func: (host: Host, old: VisibilityState, new: VisibilityState) -> Unit
) = object  : VisibilityStateListener {
    override fun onVisibilityStateChanged(host: Host, old: VisibilityState, new: VisibilityState) =
            func(host, old, new)
}

/**
 * Calls [func] when view gets shown for first time
 * either because it's created or focused (userVisibleHint).
 */
inline fun ViewFirstShownListener(
        crossinline func: (host: Host) -> Unit
) = object : VisibilityStateListener {
    var called = false
    override fun onVisibilityStateChanged(host: Host, old: VisibilityState, new: VisibilityState) {
        if (new == VisibilityState.Visible && !called) {
            func(host)
            called = true
        } else if (new == VisibilityState.Uninitialized) {
            called = false
        }
    }
}
