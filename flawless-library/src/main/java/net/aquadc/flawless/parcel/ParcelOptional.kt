package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelOptional<out P : Parcelable?>private constructor(
        val value: P
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(value, flags)
    }

    companion object CREATOR : Parcelable.Creator<ParcelOptional<*>> {

        private val empty = ParcelOptional(null)

        operator fun <T : Parcelable?> invoke(value: T) =
                if (value == null) empty else ParcelOptional(value)

        override fun createFromParcel(parcel: Parcel): ParcelOptional<*> =
                invoke<Parcelable?>(parcel.readParcelable(ParcelOptional::class.java.classLoader))

        override fun newArray(size: Int): Array<ParcelOptional<*>?> =
                arrayOfNulls(size)
    }

}
