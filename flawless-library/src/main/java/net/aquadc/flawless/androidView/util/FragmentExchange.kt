package net.aquadc.flawless.androidView.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.SparseArray
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.AnyPresenter
import net.aquadc.flawless.implementMe.Presenter
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
) : Host.Exchange<RET>, Parcelable {

    internal constructor(fragment: Fragment) : this(SparseArray(0), SparseArray(0), SparseArray(0)) {
        this.fragment = fragment
    }

    internal var fragment: Fragment? = null

    override fun <PRESENTER : AnyPresenter, RET> registerResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
            cancellationCallback: ParcelFunction1<PRESENTER, Unit>
    ) = addOrThrow(this, requestCode, resultCallback, cancellationCallback)

    override fun <PRESENTER : AnyPresenter> registerRawResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction3<PRESENTER, Int, Intent?, Unit>
    ) = addRawOrThrow(this, requestCode, resultCallback)

    override fun <PRESENTER : AnyPresenter> registerPermissionResultCallback(
            requestCode: Int, onResult: ParcelFunction2<PRESENTER, Collection<String>, Unit>
    ) = addPermissionOrThrow(this, requestCode, onResult)

    override fun <PRESENTER : AnyPresenter> startActivity(
            intent: Intent, requestCode: Int, onResult: ParcelFunction3<PRESENTER, Int, Intent?, Unit>, options: Bundle?
    ) {
        addRawOrThrow(this, requestCode, onResult)
        fragment!!.startActivityForResult(intent, requestCode, options)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        fragment!!.startActivity(intent, options)
    }

    override val hasTarget: Boolean get() = fragment!!.targetFragment != null

    override fun deliverResult(obj: RET) {
        val frag = fragment!!
        frag.targetFragment.onActivityResult(
                frag.targetRequestCode, Activity.RESULT_OK, Intent().also { it.putExtra("data", obj) }
        )
    }

    override fun deliverCancellation() {
        val frag = fragment!!
        frag.targetFragment.onActivityResult(frag.targetRequestCode, Activity.RESULT_CANCELED, null)
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

    fun deliverResult(presenter: Presenter<*, *, *, *, *, *>, requestCode: Int, responseCode: Int, data: Intent?): Boolean {
        rawCallbacks[requestCode]?.let { rawCb ->
            rawCallbacks.remove(requestCode)

            rawCb as (Any, Int, Intent?) -> Unit
            rawCb(presenter, responseCode, data)
            return true
        }

        callbacks[requestCode]?.let { (resultCb, errorCb) ->

            if (responseCode != Activity.RESULT_OK && responseCode != Activity.RESULT_CANCELED) {
                error("response code is $responseCode, " +
                        "expected RESULT_OK(${Activity.RESULT_OK}) or RESULT_CANCELED(${Activity.RESULT_CANCELED})")
            }

            callbacks.remove(requestCode)

            when (responseCode) {
                Activity.RESULT_OK -> (resultCb as (Presenter<*, *, *, *, *, *>, Any) -> Unit)(presenter, data!!.getParcelableExtra("data"))
                Activity.RESULT_CANCELED -> (errorCb as (Presenter<*, *, *, *, *, *>) -> Unit)(presenter)
                else -> throw AssertionError()
            }

            return true
        }

        return false
    }

    fun deliverPermissionResult(
            presenter: Presenter<*, *, *, *, *, *>, requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        val pcb = permCallbacks[requestCode]
                ?: return
        permCallbacks.remove(requestCode)
        pcb as (Presenter<*, *, *, *, *, *>, Collection<String>) -> Unit
        pcb(presenter, permissions.filterIndexed { idx, _ -> grantResults[idx] == PackageManager.PERMISSION_GRANTED })
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
