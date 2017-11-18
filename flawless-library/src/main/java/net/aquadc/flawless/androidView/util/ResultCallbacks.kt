package net.aquadc.flawless.androidView.util

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3

private typealias BiConsumer = ParcelFunction2<*, *, Unit>
private typealias Consumer = ParcelFunction1<*, Unit>

private typealias RawCallback = ParcelFunction3<*, Int, Intent?, Unit>


internal class ResultCallbacks internal constructor(
        private val rawCallbacks: SparseArray<RawCallback>,
        private val callbacks: SparseArray<Pair<BiConsumer, Consumer>>
) : Parcelable {

    internal constructor() : this(SparseArray(0), SparseArray(0))

    fun addOrThrow(host: Any, requestCode: Int, resultCallback: BiConsumer, cancellationCallback: Consumer) {
        assertRcFree(requestCode, host, { false }, { "callback pair ($resultCallback, $cancellationCallback)" })
        callbacks.put(requestCode, Pair(resultCallback, cancellationCallback))
    }

    fun addRawOrThrow(host: Any, requestCode: Int, callback: RawCallback) {
        assertRcFree(requestCode, host, { callback.javaClass === it.javaClass }, { "raw callback $callback" })
        rawCallbacks.put(requestCode, callback)
    }

    private inline fun assertRcFree(rc: Int, host: Any, sameCallback: (RawCallback) -> Boolean, description: () -> String) {
        (callbacks[rc] ?: rawCallbacks[rc])?.let {
            error("Attempt to add onActivityResult ${description()} to $host " +
                    "which already contains $it " +
                    "for request code $rc. " +
                    "Make sure that every started fragment delivers results, even when gets canceled. " +
                    "Registered callbacks: $callbacks and $rawCallbacks")
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
                Activity.RESULT_OK -> (resultCb as (Any, Any) -> Unit)(presenter, data!!.getParcelableExtra("data"))
                Activity.RESULT_CANCELED -> (errorCb as (Any) -> Unit)(presenter)
                else -> throw AssertionError()
            }

            return true
        }

        return false
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
    }

    companion object CREATOR : Parcelable.Creator<ResultCallbacks> {
        override fun createFromParcel(source: Parcel): ResultCallbacks {
            val cl = CREATOR::class.java.classLoader // arbitrary classLoader from client code
            return ResultCallbacks(
                    source.readSparseArray { readParcelable<RawCallback>(cl) },
                    source.readSparseArray { Pair(readParcelable<BiConsumer>(cl), readParcelable<Consumer>(cl)) }
            )
        }

        override fun newArray(size: Int): Array<ResultCallbacks?> =
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
