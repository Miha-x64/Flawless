package net.aquadc.flawless.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.androidView.MvpV4DialogFragment

/**
 * Delivers result of this dialog fragment invocation to [Fragment.onActivityResult] of [Fragment.getTargetFragment].
 */
fun <RET : Parcelable> MvpV4DialogFragment<*, RET>.deliverResult(obj: RET) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}
