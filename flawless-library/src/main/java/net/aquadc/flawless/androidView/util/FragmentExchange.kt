package net.aquadc.flawless.androidView.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.SparseArray
import net.aquadc.flawless.androidView.ContextHost
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.Screen
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3

private typealias BiConsumer = ParcelFunction2<*, *, Unit>
private typealias Consumer = ParcelFunction1<*, Unit>

private typealias RawCallback = ParcelFunction3<*, Int, Intent?, Unit>

private typealias PermissionCallback = ParcelFunction2<*, Collection<String>, Unit>


internal class FragmentExchange<RET : Parcelable> internal constructor(
        private val rawCallbacks: SparseArray<RawCallback>,
        private val callbacks: SparseArray<Pair<BiConsumer, Consumer>>,
        private val permCallbacks: SparseArray<PermissionCallback>
) : ContextHost.Exchange, Parcelable {

    internal constructor(
            fragment: Fragment, screen: AnyScreen
    ) : this(SparseArray(0), SparseArray(0), SparseArray(0)) {
        this.fragment = fragment
        this.screen = screen
    }

    private var fragment: Fragment? = null
    private var screen: AnyScreen? = null

    internal fun attachTo(fragment: Fragment?, screen: AnyScreen?) {
        check((fragment == null) == (screen == null))
        this.fragment = fragment
        this.screen = screen
    }

    override fun <SCR : AnyScreen, RET> registerResultCallback(
            screen: SCR,
            requestCode: Int,
            resultCallback: ParcelFunction2<SCR, RET, Unit>,
            cancellationCallback: ParcelFunction1<SCR, Unit>
    ) {
        checkScreen(screen)
        addOrThrow(this, requestCode, resultCallback, cancellationCallback)
    }

    override fun <SCR : AnyScreen> registerRawResultCallback(
            screen: SCR,
            requestCode: Int,
            resultCallback: ParcelFunction3<SCR, Int, Intent?, Unit>
    ) {
        checkScreen(screen)
        addRawOrThrow(this, requestCode, resultCallback)
    }

    override fun <SCR : AnyScreen> registerPermissionResultCallback(
            screen: SCR, requestCode: Int, onResult: ParcelFunction2<SCR, Collection<String>, Unit>
    ) {
        checkScreen(screen)
        addPermissionOrThrow(this, requestCode, onResult)
    }

    override fun <SCR : AnyScreen> startActivity(
            screen: SCR, intent: Intent, requestCode: Int,
            onResult: ParcelFunction3<SCR, Int, Intent?, Unit>, options: Bundle?
    ) {
        checkScreen(screen)
        addRawOrThrow(this, requestCode, onResult)
        fragment!!.startActivityForResult(intent, requestCode, options)
    }

    private fun checkScreen(actual: AnyScreen) {
        check(this.screen === actual) {
            "Attempt to set a callback from wrong screen, $actual, while hosting ${this.screen}. " +
                    "Should pass ProxyHost to encapsulated screen(s) when using decorators and/or composites."
        }
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        fragment!!.startActivity(intent, options)
    }

    override val hasTarget: Boolean get() = fragment!!.targetFragment != null

    fun deliver(obj: RET?) {
        val frag = fragment!!

        frag.targetFragment.onActivityResult(
                frag.targetRequestCode,
                if (obj == null) Activity.RESULT_CANCELED else Activity.RESULT_OK,
                if (obj == null) null else Intent().also { it.putExtra("data", obj) }
        )
    }

    fun addOrThrow(host: Any, requestCode: Int, resultCallback: BiConsumer, cancellationCallback: Consumer) {
        assertRcFree(requestCode, false, host, { false }, { "onActivityResult callback pair ($resultCallback, $cancellationCallback)" })
        callbacks.put(requestCode, Pair(resultCallback, cancellationCallback))
    }

    fun addRawOrThrow(host: Any, requestCode: Int, callback: RawCallback) {
        assertRcFree(requestCode, false, host, { callback.javaClass === it }, { "onActivityResult raw callback $callback" })
        rawCallbacks.put(requestCode, callback)
    }

    fun addPermissionOrThrow(host: Any, requestCode: Int, permCb: PermissionCallback) {
        assertRcFree(requestCode, true, host, { false }, { "onRequestPermissionResult" })
        permCallbacks.put(requestCode, permCb)
    }

    private inline fun assertRcFree(rc: Int, perm: Boolean, host: Any, sameCallback: (Class<*>) -> Boolean, description: () -> String) {
        (if (perm) permCallbacks[rc] else (callbacks[rc] ?: rawCallbacks[rc]))?.let { actual ->
            if (sameCallback(actual.javaClass)) return // lenient with raw callbacks
            error("Attempt to add ${description()} to $host which already contains $actual for request code $rc. " +
                    "Make sure that every started fragment delivers results, even when gets canceled. " +
                    "Registered callbacks: " + if (perm) permCallbacks else "$callbacks and $rawCallbacks")
        }
    }

    fun deliverResult(screen: Screen<*, *, *, *, *, *>, requestCode: Int, responseCode: Int, data: Intent?): Boolean {
        rawCallbacks[requestCode]?.let { rawCb ->
            rawCallbacks.remove(requestCode)

            rawCb as (Any, Int, Intent?) -> Unit
            rawCb(screen, responseCode, data)
            return true
        }

        callbacks[requestCode]?.let { (resultCb, errorCb) ->

            if (responseCode != Activity.RESULT_OK && responseCode != Activity.RESULT_CANCELED) {
                error("response code is $responseCode, " +
                        "expected RESULT_OK(${Activity.RESULT_OK}) or RESULT_CANCELED(${Activity.RESULT_CANCELED})")
            }

            callbacks.remove(requestCode)

            when (responseCode) {
                Activity.RESULT_OK -> (resultCb as (Screen<*, *, *, *, *, *>, Any) -> Unit)(screen, data!!.getParcelableExtra("data"))
                Activity.RESULT_CANCELED -> (errorCb as (Screen<*, *, *, *, *, *>) -> Unit)(screen)
                else -> throw AssertionError()
            }

            return true
        }

        return false
    }

    fun deliverPermissionResult(
            screen: Screen<*, *, *, *, *, *>, requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        val pcb = permCallbacks[requestCode]
                ?: return
        permCallbacks.remove(requestCode)
        pcb as (Screen<*, *, *, *, *, *>, Collection<String>) -> Unit
        pcb(screen, permissions.filterIndexed { idx, _ -> grantResults[idx] == PackageManager.PERMISSION_GRANTED })
    }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSparseArray(rawCallbacks) { rcb ->
            dest.writeParcelable(rcb, rcb.describeContents())
        }
        dest.writeSparseArray(callbacks) { (f, s) ->
            dest.writeParcelable(f, f.describeContents())
            dest.writeParcelable(s, s.describeContents())
        }
        dest.writeSparseArray(permCallbacks) { pcb ->
            dest.writeParcelable(pcb, pcb.describeContents())
        }
    }

    companion object CREATOR : Parcelable.Creator<FragmentExchange<Parcelable>> {
        override fun createFromParcel(source: Parcel): FragmentExchange<Parcelable> {
            val cl = CREATOR::class.java.classLoader // arbitrary classLoader from client code
            return FragmentExchange(
                    source.readSparseArray { readParcelable<RawCallback>(cl) },
                    source.readSparseArray { Pair(readParcelable<BiConsumer>(cl), readParcelable<Consumer>(cl)) },
                    source.readSparseArray { readParcelable<PermissionCallback>(cl) }
            )
        }

        override fun newArray(size: Int): Array<FragmentExchange<Parcelable>?> =
                arrayOfNulls(size)
    }

}

private inline fun <T> Parcel.writeSparseArray(sp: SparseArray<T>, writeT: Parcel.(T) -> Unit) {
    val size = sp.size()
    writeInt(size)
    for (i in 0 until size) {
        writeInt(sp.keyAt(i))
        writeT(sp.valueAt(i))
    }
}

private inline fun <T> Parcel.readSparseArray(readT: Parcel.() -> T): SparseArray<T> {
    val size = readInt()
    val map = SparseArray<T>(size)
    repeat(size) {
        map.put(readInt(), readT())
    }
    return map
}
