package net.aquadc.flawless.androidView

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import net.aquadc.flawless.screen.VisibilityState
import net.aquadc.flawless.screen.AnyScreen
import net.aquadc.flawless.screen.Screen
import net.aquadc.flawless.screen.VisibilityStateListener
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3

/**
 * Describes platform view which is a host for Screen.
 */
interface Host {

    /**
     * Current visibility state.
     */
    val visibilityState: VisibilityState
    // hmm, should I use observable subject for this?

    /**
     * Adds a listener which must be notified about [visibilityState] changes.
     */
    fun addVisibilityStateListener(listener: VisibilityStateListener)

    /**
     * Removes given listener. If no such listener, does nothing.
     */
    fun removeVisibilityStateListener(listener: VisibilityStateListener)

}

/**
 * Android-specific Host subtype connected to [Context]
 */
interface ContextHost : Host {

    /**
     * Describes Host's relations with outer world, i. e. Activities, Runtime Permissions.
     */
    val exchange: Exchange

    /**
     * Responsible for interaction with other hosts/screens in platform-dependent way.
     */
    interface Exchange {

        /**
         * Registers a callback which will be called within `onActivityResult`.
         */
        fun <SCR : AnyScreen, RET> registerResultCallback(
                screen: SCR,
                requestCode: Int,
                resultCallback: ParcelFunction2<SCR, RET, Unit>,
                cancellationCallback: ParcelFunction1<SCR, Unit>
        )

        /**
         * Registers a weak-typed callback which will be called within `onActivityResult`.
         */
        fun <SCR : AnyScreen> registerRawResultCallback(
                screen: SCR,
                requestCode: Int,
                resultCallback: ParcelFunction3<SCR, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>
        )

        /**
         * Registers a callback which will be called within `onRequestPermissionResult`.
         */
        fun <SCR : AnyScreen> registerPermissionResultCallback(
                screen: SCR,
                requestCode: Int,
                onResult: ParcelFunction2<SCR, @ParameterName("grantedPermissions") Collection<String>, Unit>
        )

        /**
         * Starts an Activity without requestCode.
         */
        fun startActivity(intent: Intent, options: Bundle?)

        /**
         * Starts an Activity with requestCode and a result callback.
         */
        fun <SCR : AnyScreen> startActivity(
                screen: SCR,
                intent: Intent,
                requestCode: Int,
                onResult: ParcelFunction3<SCR, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>,
                options: Bundle?
        )

        /**
         * Determines whether this host has a target,
         * which waits for result or cancellation.
         */
        val hasTarget: Boolean

    }

}

@Suppress("NOTHING_TO_INLINE") // really small
inline fun <SCR : AnyScreen, RET : Parcelable> ContextHost.Exchange.registerResultCallback(
        screen: SCR,
        requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>
): Unit = registerResultCallback(screen, requestCode, resultCallback, NoOpParcelFunction1)

@Suppress("NOTHING_TO_INLINE") // small
fun <SCR : AnyScreen> ContextHost.Exchange.startActivity(
        screen: SCR,
        intent: Intent,
        requestCode: Int,
        onResult: ParcelFunction3<SCR, @ParameterName("responseCode") Int, @ParameterName("data") Intent?, Unit>
): Unit = startActivity(screen, intent, requestCode, onResult, null)

@Suppress("NOTHING_TO_INLINE") // small
fun ContextHost.Exchange.startActivity(intent: Intent): Unit = startActivity(intent, null)

/**
 * Runs specified code previously asking for permissions, if necessary.
 */
inline fun <HOST, SCR : Screen<*, *, HOST, *, *, *>>
        HOST.requestPermissions(
        screen: SCR,
        requestCode: Int,
        onResult: ParcelFunction2<SCR, @ParameterName("granted") Collection<String>, Unit>,
        showRationale: (forPermissions: List<String>, userAgreed: Runnable) -> Unit,
        vararg permissions: String
) where HOST : ContextHost, HOST : Fragment {
    if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
        exchange.registerPermissionResultCallback(screen, requestCode, onResult)
        // make the same array as system makes and passes to onRequestPermissionResult:
        val allGranted = IntArray(permissions.size) { PackageManager.PERMISSION_GRANTED }
        return onRequestPermissionsResult(requestCode, permissions, allGranted)
    }

    val permissionsToShowRationale = permissions
            .filter { shouldShowRequestPermissionRationale(it) }

    if (permissionsToShowRationale.isNotEmpty()) {
        return showRationale(
                permissionsToShowRationale,
                RequestPerms(this, screen, requestCode, onResult, permissions)
        )
    }

    exchange.registerPermissionResultCallback(screen, requestCode, onResult)
    requestPermissions(permissions, requestCode)
}

// I don't want this Runnable to be copied into every call-site, so it is not anonymous
@PublishedApi internal class RequestPerms<HOST, SCR : AnyScreen>(
        private val host: HOST,
        private val screen: SCR,
        private val requestCode: Int,
        private val onResult: ParcelFunction2<SCR, Collection<String>, Unit>,
        private val permissions: Array<out String>
) : Runnable where HOST : ContextHost, HOST : Fragment {
    override fun run() {
        host.exchange.registerPermissionResultCallback(screen, requestCode, onResult)
        host.requestPermissions(permissions, requestCode)
    }
}
