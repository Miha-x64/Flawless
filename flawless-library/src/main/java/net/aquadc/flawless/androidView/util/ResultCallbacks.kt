package net.aquadc.flawless.androidView.util

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2

private typealias BiConsumer = ParcelFunction2<*, *, Unit>
private typealias Consumer = ParcelFunction1<*, Unit>

internal class ResultCallbacks internal constructor(
        private val callbacks: SparseArray<Pair<BiConsumer, Consumer>>
) : Parcelable {

    internal constructor() : this(SparseArray(1))

    fun addOrThrow(requestCode: Int,
                   resultCallback: BiConsumer, cancellationCallback: Consumer) {
        callbacks[requestCode]?.let {
            error("Attempt to add onActivityResult callback pair ($resultCallback, $cancellationCallback) to fragment " +
                    "which already contains callbacks (${it.first}, ${it.second}) " +
                    "for request code $requestCode. " +
                    "Make sure that every started fragment delivers results, even when gets canceled. " +
                    "Registered callbacks: $callbacks")
        }

        callbacks.put(requestCode, Pair(resultCallback, cancellationCallback))
    }

    fun deliverResult(presenter: Presenter<*, *, *, *, *, *>, requestCode: Int, responseCode: Int, data: Intent?): Boolean {
        val (resultCb, errorCb) = callbacks[requestCode]
                ?: return false

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

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        val cbs = callbacks
        dest.writeInt(cbs.size())
        for (i in 0 until cbs.size()) {
            dest.writeInt(cbs.keyAt(i))
            val (f, s) = cbs.valueAt(i)
            dest.writeParcelable(f, f.describeContents())
            dest.writeParcelable(s, s.describeContents())
        }
    }

    companion object CREATOR : Parcelable.Creator<ResultCallbacks> {
        override fun createFromParcel(source: Parcel): ResultCallbacks {
            val size = source.readInt()
            val map = SparseArray<Pair<BiConsumer, Consumer>>(size)
            val cl = CREATOR::class.java.classLoader // whatever classLoader from client code
            repeat(size) {
                map.put(source.readInt(), Pair(source.readParcelable(cl), source.readParcelable(cl)))
            }
            return ResultCallbacks(map)
        }

        override fun newArray(size: Int): Array<ResultCallbacks?> =
                arrayOfNulls(size)
    }

}
