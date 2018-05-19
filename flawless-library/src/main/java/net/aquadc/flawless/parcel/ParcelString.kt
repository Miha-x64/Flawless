package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelString(
        val value: String
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(value)
    }

    @Suppress("NOTHING_TO_INLINE") inline operator fun component1() = value

    companion object CREATOR : Parcelable.Creator<ParcelString> {
        override fun createFromParcel(source: Parcel): ParcelString =
                ParcelString(source.readString())

        override fun newArray(size: Int): Array<ParcelString?> =
                arrayOfNulls(size)
    }

}
