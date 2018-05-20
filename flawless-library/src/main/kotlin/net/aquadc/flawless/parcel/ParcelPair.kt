package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


@Suppress("NOTHING_TO_INLINE")
class ParcelPair<out A : Parcelable?, out B : Parcelable?>(
        val a: A,
        val b: B
) : Parcelable {

    override fun describeContents(): Int =
            (a?.describeContents() ?: 0) or
                    (b?.describeContents() ?: 0)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(a, flags)
        dest.writeParcelable(b, flags)
    }

    inline operator fun component1() = a
    inline operator fun component2() = b

    companion object CREATOR : Parcelable.Creator<ParcelPair<*, *>> {

        override fun createFromParcel(source: Parcel): ParcelPair<*, *> {
            val cl = ParcelPair::class.java.classLoader
            return ParcelPair(source.readParcelable<Parcelable>(cl), source.readParcelable<Parcelable>(cl))
        }

        override fun newArray(size: Int): Array<ParcelPair<*, *>?> = arrayOfNulls(size)

    }

}
