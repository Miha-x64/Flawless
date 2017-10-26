package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable

class ParcelList<out E : Parcelable>(
        private val list: List<E>
) : Parcelable, List<E> by list {

    override fun describeContents(): Int = 0 // lol, hope no file descrptors in a list
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelableArray(list.toTypedArray<Parcelable>(), flags)
    }

    companion object CREATOR : Parcelable.Creator<ParcelList<*>> {
        override fun newArray(size: Int): Array<ParcelList<*>?> = arrayOfNulls(size)
        override fun createFromParcel(source: Parcel): ParcelList<*> =
                ParcelList(source.readParcelableArray(ParcelList::class.java.classLoader).asList())
    }

}
