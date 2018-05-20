package android.support.v4.app

import android.os.Bundle

/**
 * `savedInstanceState` is being passed into [Fragment.onCreate],
 * but we want it in [Fragment.onAttach].
 * Go get it, it's initialized early.
 */
internal val Fragment.spySavedState: Bundle?
    @JvmSynthetic get() = mSavedFragmentState
