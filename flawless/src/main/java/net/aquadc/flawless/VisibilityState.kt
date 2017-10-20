package net.aquadc.flawless

import android.support.v4.app.Fragment

/**
 * Represents View's visibility state.
 * [Uninitialized] means that view was not created yes.
 * [Invisible] means that view already exists, but invisible for user because [Fragment.getUserVisibleHint] is false.
 * [Visible] means that view exists and [Fragment.getUserVisibleHint] is true.
 */
enum class VisibilityState {
    Uninitialized, Invisible, Visible
}
