package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelPair<out A : Parcelable, out B : Parcelable>(
        val a: A,
        val b: B
) : Parcelable {

    override fun describeContents(): Int = a.describeContents() or b.describeContents()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(a, flags)
        dest.writeParcelable(b, flags)
    }

    companion object CREATOR : Parcelable.Creator<ParcelPair<*, *>> {

        override fun createFromParcel(source: Parcel): ParcelPair<*, *> {
            val cl = ParcelPair::class.java.classLoader
            return ParcelPair(source.readParcelable<Parcelable>(cl), source.readParcelable<Parcelable>(cl))
        }

        override fun newArray(size: Int): Array<ParcelPair<*, *>?> = arrayOfNulls(size)

    }

}
