package net.aquadc.flawless.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.tag.V4DialogFragPresenterTag
import net.aquadc.flawless.tag.V4FragPresenterTag

fun Fragment.deliverCancellation() {
    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
}

fun <PR : Presenter<*, *, *, *, *, *>, TAR : Parcelable, RET : Parcelable>
        MvpV4Fragment<*, *>.createFragmentForResult(
        newFragmentTag: V4FragPresenterTag<TAR, RET, *>,
        arg: TAR,
        requestCode: Int,
        callback: ParcelFunction2<PR, RET, Unit>
): MvpV4Fragment<TAR, RET> {
    val newFragment = MvpV4Fragment(newFragmentTag, arg)
    newFragment.setTargetFragment(this, requestCode)
    registerResultCallback(requestCode, callback)
    return newFragment
}

fun <RET : Parcelable> MvpV4Fragment<*, RET>.deliverResult(obj: RET) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}



fun <PR : Presenter<*, *, *, *, *, *>, TAR : Parcelable, RET : Parcelable>
        MvpV4Fragment<*, *>.createDialogFragmentForResult(
        newFragmentTag: V4DialogFragPresenterTag<TAR, RET, *>,
        arg: TAR,
        requestCode: Int,
        callback: ParcelFunction2<PR, RET, Unit>
): MvpV4DialogFragment<TAR, RET> {
    val newFragment = MvpV4DialogFragment(newFragmentTag, arg)
    newFragment.setTargetFragment(this, requestCode)
    registerResultCallback(requestCode, callback)
    return newFragment
}

fun <RET : Parcelable> MvpV4DialogFragment<*, RET>.deliverResult(obj: RET) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}


// todo: willStartForResult with MvpV4DialogFragment


inline fun MvpV4Fragment<*, *>.addVisibilityStateListener(
        crossinline listener: (host: Fragment, old: VisibilityState, new: VisibilityState) -> Unit
) {
    addVisibilityStateListener(object : VisibilityStateListener {
        override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) =
                listener(host, old, new)
    })
}

inline fun <ARG : Parcelable> MvpV4Fragment<ARG, *>.addViewFirstShownListener(
        crossinline callback: (MvpV4Fragment<ARG, *>) -> Unit
) {
    addVisibilityStateListener(
            object : VisibilityStateListener {
                override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) {
                    if (new == VisibilityState.Visible) {
                        this@addViewFirstShownListener.removeVisibilityStateListener(this)
                        callback(this@addViewFirstShownListener)
                    }
                }
            })
}
