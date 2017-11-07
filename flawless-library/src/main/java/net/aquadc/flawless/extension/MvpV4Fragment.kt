package net.aquadc.flawless.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.V4FragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.tag.V4DialogFragPresenterTag
import net.aquadc.flawless.tag.V4FragPresenterTag

/**
 * Creates new [MvpV4Fragment] with [newFragmentTag] and sets its `targetFragment` to `this`.
 * @param PR this presenter
 * @param ARG argument type of new fragment
 * @param RET return type of new fragment
 */
fun <PR : V4FragPresenter<*, *, *>, ARG : Parcelable, RET : Parcelable>
        MvpV4Fragment<*, *>.createFragmentForResult(
        newFragmentTag: V4FragPresenterTag<ARG, RET, *>,
        arg: ARG,
        requestCode: Int,
        resultCallback: ParcelFunction2<PR, RET, Unit>,
        cancellationCallback: ParcelFunction1<PR, Unit> = NoOpParcelFunction1
): MvpV4Fragment<ARG, RET> = MvpV4Fragment(newFragmentTag, arg).also { new ->
    new.setTargetFragment(this, requestCode)
    this.registerResultCallback(requestCode, resultCallback, cancellationCallback)
}


/**
 * Creates new [MvpV4DialogFragment] with [newFragmentTag] and sets its `targetFragment` to `this`.
 * @param PR this presenter
 * @param ARG argument type of new fragment
 * @param RET return type of new fragment
 */
fun <PR : Presenter<*, *, *, *, *, *>, ARG : Parcelable, RET : Parcelable>
        MvpV4Fragment<*, *>.createDialogFragmentForResult(
        newFragmentTag: V4DialogFragPresenterTag<ARG, RET, *>,
        arg: ARG,
        requestCode: Int,
        resultCallback: ParcelFunction2<PR, RET, Unit>,
        cancellationCallback: ParcelFunction1<PR, Unit> = NoOpParcelFunction1
): MvpV4DialogFragment<ARG, RET> = MvpV4DialogFragment(newFragmentTag, arg).also { new ->
    new.setTargetFragment(this, requestCode)
    this.registerResultCallback(requestCode, resultCallback, cancellationCallback)
}


/**
 * Delivers result of this fragment invocation to [Fragment.onActivityResult] of [Fragment.getTargetFragment].
 */
fun <RET : Parcelable> MvpV4Fragment<*, RET>.deliverResult(obj: RET) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}


/**
 * Registers given function as a listener of [VisibilityState] updates.
 */
inline fun MvpV4Fragment<*, *>.addVisibilityStateListener(
        crossinline listener: (host: Fragment, old: VisibilityState, new: VisibilityState) -> Unit
) {
    addVisibilityStateListener(object : VisibilityStateListener {
        override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) =
                listener(host, old, new)
    })
}

/**
 * Registers given function as a listener of first [VisibilityState] change to [VisibilityState.Visible].
 * Data should be loaded here.
 */
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
