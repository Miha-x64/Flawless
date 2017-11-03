package net.aquadc.flawless.solution

import android.os.Parcel
import android.os.Parcelable

sealed class LoadingResult<out T : Parcelable> : Parcelable {

    class Success<out T : Parcelable>(val value: T) : LoadingResult<T>() {
        override fun describeContents(): Int = value.describeContents()
        override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeParcelable(value, flags)
        companion object CREATOR : Parcelable.Creator<Success<*>> {
            override fun newArray(size: Int): Array<Success<*>?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): Success<*> =
                    Success(source.readParcelable<Parcelable>(Success::class.java.classLoader))
        }
    }

    class Error(val exception: Throwable) : LoadingResult<Nothing>() {
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeSerializable(exception)
        companion object CREATOR : Parcelable.Creator<Error> {
            override fun newArray(size: Int): Array<Error?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): Error =
                    Error(source.readSerializable() as Throwable)
        }
    }

}

inline fun <T : Parcelable, R> LoadingResult<T>.let(
        onSuccess: (T) -> R,
        onError: (Throwable) -> R
) = when (this) {
    is LoadingResult.Success -> onSuccess(value)
    is LoadingResult.Error -> onError(exception)
}
