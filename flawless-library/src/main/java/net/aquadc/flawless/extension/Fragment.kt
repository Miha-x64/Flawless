package net.aquadc.flawless.extension

import android.app.Activity
import android.support.v4.app.Fragment

/**
 * Invokes [Fragment.onActivityResult] with [Activity.RESULT_CANCELED] and `null` data.
 */
fun Fragment.deliverCancellation() {
    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
}
