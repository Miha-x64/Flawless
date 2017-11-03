package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable

object ParcelUnit : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = Unit

    @[Suppress("UNUSED") JvmField]
    val CREATOR = object : Parcelable.Creator<ParcelUnit> {
        override fun createFromParcel(source: Parcel): ParcelUnit = ParcelUnit
        override fun newArray(size: Int): Array<ParcelUnit?> = arrayOfNulls(size)
    }

}
