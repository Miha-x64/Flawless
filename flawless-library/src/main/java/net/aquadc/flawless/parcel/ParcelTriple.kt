package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable


class ParcelTriple<out A : Parcelable?, out B : Parcelable?, out C : Parcelable?>(
        val a: A,
        val b: B,
        val c: C
) : Parcelable {

    override fun describeContents(): Int =
            (a?.describeContents() ?: 0) or
                    (b?.describeContents() ?: 0) or
                    (c?.describeContents() ?: 0)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(a, flags)
        dest.writeParcelable(b, flags)
        dest.writeParcelable(c, flags)
    }

    @Suppress("NOTHING_TO_INLINE") inline operator fun component1() = a
    @Suppress("NOTHING_TO_INLINE") inline operator fun component2() = b
    @Suppress("NOTHING_TO_INLINE") inline operator fun component3() = c

    companion object CREATOR : Parcelable.Creator<ParcelTriple<*, *, *>> {

        override fun createFromParcel(source: Parcel): ParcelTriple<*, *, *> {
            val cl = ParcelTriple::class.java.classLoader
            return ParcelTriple(source.readParcelable<Parcelable>(cl), source.readParcelable<Parcelable>(cl), source.readParcelable<Parcelable>(cl))
        }

        override fun newArray(size: Int): Array<ParcelTriple<*, *, *>?> = arrayOfNulls(size)

    }

}
