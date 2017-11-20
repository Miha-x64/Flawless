package net.aquadc.flawless

import android.support.v4.app.Fragment

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
