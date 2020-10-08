package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


sealed class ParcelResult<out T : Parcelable> : Parcelable {
    class Success<out T : Parcelable>(val value: T) : ParcelResult<T>() {
        override fun describeContents(): Int = value.describeContents()
        override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeParcelable(value, flags)

        companion object CREATOR : Parcelable.Creator<Success<Parcelable>> {
            override fun createFromParcel(parcel: Parcel): Success<Parcelable> =
                Success(parcel.readParcelable(Success::class.java.classLoader)!!)
            override fun newArray(size: Int): Array<Success<*>?> =
                arrayOfNulls(size)
        }
    }

    class Error(val exception: Throwable) : ParcelResult<Nothing>() {
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeSerializable(exception)

        companion object CREATOR : Parcelable.Creator<Error> {
            override fun createFromParcel(parcel: Parcel): Error = Error(parcel.readSerializable() as Throwable)
            override fun newArray(size: Int): Array<Error?> = arrayOfNulls(size)
        }

    }
}

inline fun <T : Parcelable, R> ParcelResult<T>.let(
        onSuccess: (T) -> R,
        onError: (Throwable) -> R
): R = when (this) {
    is ParcelResult.Success -> onSuccess(value)
    is ParcelResult.Error -> onError(exception)
}

inline fun <T : Parcelable, R : Parcelable> ParcelResult<T>.map(
        transform: (T) -> R
): ParcelResult<R> = when (this) {
    is ParcelResult.Success -> ParcelResult.Success(transform(value))
    is ParcelResult.Error -> this
}
