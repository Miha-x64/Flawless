package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelList<out E : Parcelable>(
        val value: List<E>
) : Parcelable {

    override fun describeContents(): Int = 0 // lol, hope no file descrptors in a list
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelableArray(value.toTypedArray<Parcelable>(), flags)
    }

    @Suppress("NOTHING_TO_INLINE") inline operator fun component1() = value

    companion object CREATOR : Parcelable.Creator<ParcelList<*>> {
        override fun newArray(size: Int): Array<ParcelList<*>?> = arrayOfNulls(size)
        override fun createFromParcel(source: Parcel): ParcelList<*> =
                ParcelList(source.readParcelableArray(ParcelList::class.java.classLoader).asList())
    }

}
