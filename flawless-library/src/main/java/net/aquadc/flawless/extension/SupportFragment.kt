package net.aquadc.flawless.extension

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.tag.SupportDialogFragPresenterTag
import net.aquadc.flawless.tag.SupportFragPresenterTag

/**
 * Creates new [SupportFragment] with [newFragmentTag] and sets its `targetFragment` to `this`.
 * New fragment MUST deliver either result or cancellation.
 * @param PR this presenter
 * @param ARG argument type of new fragment
 * @param RET return type of new fragment
 */
fun <PR : Presenter<*, *, *, *, *, *>, ARG : Parcelable, RET : Parcelable>
        SupportFragment<*, *>.createFragmentForResult(
        newFragmentTag: SupportFragPresenterTag<ARG, RET, *>,
        arg: ARG,
        requestCode: Int,
        resultCallback: ParcelFunction2<PR, RET, Unit>,
        cancellationCallback: ParcelFunction1<PR, Unit> = NoOpParcelFunction1
): SupportFragment<ARG, RET> = SupportFragment(newFragmentTag, arg).also { new ->
    new.setTargetFragment(this, requestCode)
    this.registerResultCallback(requestCode, resultCallback, cancellationCallback)
}


/**
 * Creates new [SupportDialogFragment] with [newFragmentTag] and sets its `targetFragment` to `this`.
 * New fragment MUST deliver either result or cancellation.
 * @param PR this presenter
 * @param ARG argument type of new fragment
 * @param RET return type of new fragment
 */
fun <PR : Presenter<*, *, *, *, *, *>, ARG : Parcelable, RET : Parcelable>
        SupportFragment<*, *>.createDialogFragmentForResult(
        newFragmentTag: SupportDialogFragPresenterTag<ARG, RET, *>,
        arg: ARG,
        requestCode: Int,
        resultCallback: ParcelFunction2<PR, RET, Unit>,
        cancellationCallback: ParcelFunction1<PR, Unit> = NoOpParcelFunction1
): SupportDialogFragment<ARG, RET> = SupportDialogFragment(newFragmentTag, arg).also { new ->
    new.setTargetFragment(this, requestCode)
    this.registerResultCallback(requestCode, resultCallback, cancellationCallback)
}


/**
 * Runs specified code with requested permissions.
 */
inline fun <PRESENTER : Presenter<*, *, *, *, *, *>>
        SupportFragment<*, *>.requestPermissions(
        requestCode: Int,
        onResult: ParcelFunction2<PRESENTER, @ParameterName("granted") Collection<String>, Unit>,
        showRationale: (forPermissions: List<String>, userAgreed: Runnable) -> Unit,
        vararg permissions: String
) {
    if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
        registerPermissionResultCallback(requestCode, onResult)
        return onRequestPermissionsResult(requestCode, permissions, permissions.map { PackageManager.PERMISSION_GRANTED }.toIntArray())
    }

    val permissionsToShowRationale = permissions
            .filter { shouldShowRequestPermissionRationale(it) }

    if (permissionsToShowRationale.isNotEmpty()) {
        return showRationale(permissionsToShowRationale, Runnable {
            registerPermissionResultCallback(requestCode, onResult)
            requestPermissions(permissions, requestCode)
        })
    }

    registerPermissionResultCallback(requestCode, onResult)
    requestPermissions(permissions, requestCode)
}


/**
 * Delivers result of this fragment invocation to [Fragment.onActivityResult] of [Fragment.getTargetFragment].
 */
fun <RET : Parcelable> SupportFragment<*, RET>.deliverResult(obj: RET) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}


/**
 * Registers given function as a listener of [VisibilityState] updates.
 */
inline fun SupportFragment<*, *>.addVisibilityStateListener(
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
inline fun <ARG : Parcelable> SupportFragment<ARG, *>.addViewFirstShownListener(
        crossinline callback: (SupportFragment<ARG, *>) -> Unit
) {
    addVisibilityStateListener(
            object : VisibilityStateListener {
                var called = false
                override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) {
                    if (new == VisibilityState.Visible && !called) {
                        callback(this@addViewFirstShownListener)
                        called = true
                    } else if (new == VisibilityState.Uninitialized) {
                        called = false
                    }
                }
            })
}
