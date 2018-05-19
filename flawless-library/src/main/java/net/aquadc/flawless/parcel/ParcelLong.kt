package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelLong(
        val value: Long
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(value)
    }

    @Suppress("NOTHING_TO_INLINE") inline operator fun component1() = value

    companion object CREATOR : Parcelable.Creator<ParcelLong> {
        override fun newArray(size: Int): Array<ParcelLong?> =
                arrayOfNulls(size)

        override fun createFromParcel(source: Parcel): ParcelLong =
                ParcelLong(source.readLong())
    }

}