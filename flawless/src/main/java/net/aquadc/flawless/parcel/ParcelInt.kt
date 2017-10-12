package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable

class ParcelInt(
        val value: Int
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(value)
    }

    companion object CREATOR : Parcelable.Creator<ParcelInt> {
        override fun newArray(size: Int): Array<ParcelInt?> =
                arrayOfNulls(size)

        override fun createFromParcel(source: Parcel): ParcelInt =
                ParcelInt(source.readInt())
    }

}
