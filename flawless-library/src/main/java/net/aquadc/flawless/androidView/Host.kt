package net.aquadc.flawless.androidView

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.implementMe.AnyPresenter
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3

/**
 * Describes platform view which is a host for Presenter.
 */
interface Host</*out*/ RET : Parcelable> {


    /**
     * Current visibility state.
     */
    val visibilityState: VisibilityState

    /**
     * Adds a listener which must be notified about [visibilityState] changes.
     */
    fun addVisibilityStateListener(listener: VisibilityStateListener)

    /**
     * Removes listener. If no such listener, does nothing.
     */
    fun removeVisibilityStateListener(listener: VisibilityStateListener)


    val exchange: Exchange<RET>

    /**
     * Responsible for interaction with other hosts/presenter in platform-dependent way.
     */
    interface Exchange</*out*/ RET : Parcelable> {

        fun <PRESENTER : AnyPresenter, RET> registerResultCallback(
                requestCode: Int,
                resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
                cancellationCallback: ParcelFunction1<PRESENTER, Unit>
        )

        fun <PRESENTER : AnyPresenter> registerRawResultCallback(
                requestCode: Int,
                resultCallback: ParcelFunction3<PRESENTER, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>
        )

        fun <PRESENTER : AnyPresenter> registerPermissionResultCallback(
                requestCode: Int,
                onResult: ParcelFunction2<PRESENTER, @ParameterName("grantedPermissions") Collection<String>, Unit>
        )

        fun startActivity(intent: Intent, options: Bundle?)

        fun <PRESENTER : AnyPresenter> startActivity(
                intent: Intent,
                requestCode: Int,
                onResult: ParcelFunction3<PRESENTER, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>,
                options: Bundle?
        )

        /**
         * Determines whether this host has a target,
         * which waits for result or cancellation.
         */
        val hasTarget: Boolean

        /**
         * Delivers result to target. Causes exception if ![hasTarget]
         */
        @Deprecated(message = "should deliver result by means of Presenter.returnValue")
        fun deliverResult(obj: RET)

        /**
         * Delivers cancellation to target. Causes exception if ![hasTarget]
         */
        @Deprecated(message = "should deliver result by means of Presenter.returnValue")
        fun deliverCancellation()

        /**
         * Delivers result if not null, delivers cancellation otherwise.
         * Causes exception if ![hasTarget]
         */
        fun deliver(obj: RET?)

    }

}

@Suppress("NOTHING_TO_INLINE") // really small
inline fun <PRESENTER : AnyPresenter, RET : Parcelable> Host.Exchange<RET>.registerResultCallback(
        requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>
) = registerResultCallback(requestCode, resultCallback, NoOpParcelFunction1)

@Suppress("NOTHING_TO_INLINE") // small
fun <PRESENTER : AnyPresenter> Host.Exchange<*>.startActivity(
        intent: Intent,
        requestCode: Int,
        onResult: ParcelFunction3<PRESENTER, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>
) = startActivity(intent, requestCode, onResult, null)

@Suppress("NOTHING_TO_INLINE") // small
fun Host.Exchange<*>.startActivity(intent: Intent) = startActivity(intent, null)

/**
 * Registers given function as a listener of [VisibilityState] updates.
 */
inline fun <RET : Parcelable> Host<RET>.addVisibilityStateListener(
        crossinline listener: (host: Host<RET>, old: VisibilityState, new: VisibilityState) -> Unit
) {
    addVisibilityStateListener(object : VisibilityStateListener {
        override fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState) =
                listener(this@addVisibilityStateListener, old, new)
    })
}

/**
 * Registers given function as a listener of first [VisibilityState] change to [VisibilityState.Visible].
 * Data should be loaded here.
 */
inline fun <RET : Parcelable, HOST : Host<RET>> HOST.addViewFirstShownListener(
        crossinline callback: (HOST) -> Unit
) {
    addVisibilityStateListener(
            object : VisibilityStateListener {
                var called = false
                override fun onVisibilityStateChanged(host: Host<*>, old: VisibilityState, new: VisibilityState) {
                    if (new == VisibilityState.Visible && !called) {
                        callback(this@addViewFirstShownListener)
                        called = true
                    } else if (new == VisibilityState.Uninitialized) {
                        called = false
                    }
                }
            })
}

/**
 * Runs specified code previously asking for permissions, if necessary.
 */
inline fun <HOST, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        HOST.requestPermissions(
        requestCode: Int,
        onResult: ParcelFunction2<PRESENTER, @ParameterName("granted") Collection<String>, Unit>,
        showRationale: (forPermissions: List<String>, userAgreed: Runnable) -> Unit,
        vararg permissions: String
) where HOST : Host<*>, HOST : Fragment {
    if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
        exchange.registerPermissionResultCallback(requestCode, onResult)
        return onRequestPermissionsResult(requestCode, permissions, permissions.map { PackageManager.PERMISSION_GRANTED }.toIntArray())
    }

    val permissionsToShowRationale = permissions
            .filter { shouldShowRequestPermissionRationale(it) }

    if (permissionsToShowRationale.isNotEmpty()) {
        return showRationale(permissionsToShowRationale, Runnable {
            exchange.registerPermissionResultCallback(requestCode, onResult)
            requestPermissions(permissions, requestCode)
        })
    }

    exchange.registerPermissionResultCallback(requestCode, onResult)
    requestPermissions(permissions, requestCode)
}
