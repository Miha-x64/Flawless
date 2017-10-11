package net.aquadc.flawless.androidView.util

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.parcel.ParcelFunction2

internal class ResultCallbacks internal constructor(
        private val callbacks: SparseArray<ParcelFunction2<*, *, Unit>>
) : Parcelable {

    internal constructor() : this(SparseArray<ParcelFunction2<*, *, Unit>>())

    fun addOrThrow(requestCode: Int, callback: ParcelFunction2<*, *, Unit>) {
        check(callbacks[requestCode] == null) {
            "Attempt to add a result callback (#${callback.hashCode()}) to fragment " +
                    "which already contains a result callback (#${callback.hashCode()}) " +
                    "for request code $requestCode. " +
                    "Make sure that every started fragment delivers results, even when gets canceled. " +
                    "Registered callbacks: $callbacks"
        }

        callbacks.put(requestCode, callback)
    }

    fun deliverResult(presenter: Presenter<*, *, *, *, *>, requestCode: Int, responseCode: Int, data: Intent?): Boolean {
        val callback = callbacks[requestCode]
                ?: return false

        if (responseCode != Activity.RESULT_OK && responseCode != Activity.RESULT_CANCELED) {
            error("response code is $responseCode, " +
                    "expected RESULT_OK(${Activity.RESULT_OK}) or RESULT_CANCELED(${Activity.RESULT_CANCELED})")
        }

        callbacks.remove(requestCode)

        if (responseCode == Activity.RESULT_OK) {
            (callback as (Any, Any) -> Unit)(presenter, data!!.getParcelableExtra("data"))
        }

        return true
    }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSparseArray(callbacks as SparseArray<Any>)
    }

    companion object CREATOR : Parcelable.Creator<ResultCallbacks> {
        override fun createFromParcel(source: Parcel): ResultCallbacks =
                ResultCallbacks(source.readSparseArray(ParcelFunction2::class.java.classLoader)
                        as SparseArray<ParcelFunction2<*, *, Unit>>)

        override fun newArray(size: Int): Array<ResultCallbacks?> =
                arrayOfNulls(size)
    }

}
