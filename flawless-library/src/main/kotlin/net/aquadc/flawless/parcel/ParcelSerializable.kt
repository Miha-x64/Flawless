package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Reads/writes a [Serializable] instance from/to [Parcel].
 * Useful for [Enum]s.
 */
class ParcelSerializable<T : Serializable>(
        val value: T
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(value)
    }

    @Suppress("NOTHING_TO_INLINE") inline operator fun component1() = value

    companion object CREATOR : Parcelable.Creator<ParcelSerializable<*>> {
        override fun newArray(size: Int): Array<ParcelSerializable<*>?> =
                arrayOfNulls(size)

        override fun createFromParcel(source: Parcel): ParcelSerializable<*> =
                ParcelSerializable(source.readSerializable())
    }

}
