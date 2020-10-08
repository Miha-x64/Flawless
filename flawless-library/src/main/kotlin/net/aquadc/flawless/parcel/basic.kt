@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


abstract class BoringParcelable : Parcelable {
    final override fun describeContents(): Int = 0
}

object ParcelUnit : BoringParcelable() {
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = Unit
    @JvmField val CREATOR: Parcelable.Creator<ParcelUnit> = object : Parcelable.Creator<ParcelUnit> {
        override fun createFromParcel(source: Parcel): ParcelUnit = ParcelUnit
        override fun newArray(size: Int): Array<ParcelUnit?> = arrayOfNulls(size)
    }
}

class ParcelInt(@JvmField val value: Int) : BoringParcelable() {
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeInt(value)
    inline operator fun component1(): Int = value
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ParcelInt> = object : Parcelable.Creator<ParcelInt> {
            override fun createFromParcel(source: Parcel): ParcelInt = ParcelInt(source.readInt())
            override fun newArray(size: Int): Array<ParcelInt?> = arrayOfNulls(size)
        }
    }
}

class ParcelLong(@JvmField val value: Long) : BoringParcelable() {
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeLong(value)
    inline operator fun component1(): Long = value
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ParcelLong> = object : Parcelable.Creator<ParcelLong> {
            override fun createFromParcel(source: Parcel): ParcelLong = ParcelLong(source.readLong())
            override fun newArray(size: Int): Array<ParcelLong?> = arrayOfNulls(size)
        }
    }
}

class ParcelString(@JvmField val value: String) : BoringParcelable() {
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeString(value)
    inline operator fun component1(): String = value
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ParcelString> = object : Parcelable.Creator<ParcelString> {
            override fun createFromParcel(source: Parcel): ParcelString = ParcelString(source.readString()!!)
            override fun newArray(size: Int): Array<ParcelString?> = arrayOfNulls(size)
        }
    }
}

class ParcelList<out E : Parcelable>(@JvmField val value: List<E>) : Parcelable {
    override fun describeContents(): Int =
        value.fold(0) { acc, e -> acc or e.describeContents() }
    override fun writeToParcel(dest: Parcel, flags: Int): Unit =
        dest.writeParcelableArray(value.toTypedArray<Parcelable>(), flags)
    inline operator fun component1() =
        value
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ParcelList<*>> = object : Parcelable.Creator<ParcelList<*>> {
            override fun createFromParcel(source: Parcel): ParcelList<*> =
                ParcelList(source.readParcelableArray(ParcelList::class.java.classLoader)!!.asList())
            override fun newArray(size: Int): Array<ParcelList<*>?> =
                arrayOfNulls(size)
        }
    }
}

class ParcelOptional<out P : Parcelable?> private constructor(@JvmField val value: P) : Parcelable {
    override fun describeContents(): Int = value?.describeContents() ?: 0
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeParcelable(value, flags)
    inline operator fun component1(): P = value
    companion object {
        private val empty =
            ParcelOptional(null)
        operator fun <T : Parcelable?> invoke(value: T) =
            if (value == null) empty else ParcelOptional(value)
        @JvmField val CREATOR: Parcelable.Creator<ParcelOptional<*>> = object : Parcelable.Creator<ParcelOptional<*>> {
            override fun createFromParcel(parcel: Parcel): ParcelOptional<*> =
                invoke<Parcelable?>(parcel.readParcelable(ParcelOptional::class.java.classLoader))
            override fun newArray(size: Int): Array<ParcelOptional<*>?> =
                arrayOfNulls(size)
        }
    }
}


class ParcelPair<out A : Parcelable?, out B : Parcelable?>(@JvmField val a: A, @JvmField val b: B) : Parcelable {
    override fun describeContents(): Int = (a?.describeContents() ?: 0) or (b?.describeContents() ?: 0)
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(a, flags)
        dest.writeParcelable(b, flags)
    }
    inline operator fun component1(): A = a
    inline operator fun component2(): B = b

    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ParcelPair<*, *>> = object : Parcelable.Creator<ParcelPair<*, *>> {
            override fun createFromParcel(source: Parcel): ParcelPair<*, *> =
                ParcelPair::class.java.classLoader.let { cl ->
                    ParcelPair(source.readParcelable<Parcelable>(cl), source.readParcelable<Parcelable>(cl))
                }
            override fun newArray(size: Int): Array<ParcelPair<*, *>?> = arrayOfNulls(size)
        }
    }
}

class ParcelTriple<out A : Parcelable?, out B : Parcelable?, out C : Parcelable?>(
    @JvmField val a: A, @JvmField val b: B, @JvmField val c: C
) : Parcelable {
    override fun describeContents(): Int =
        (a?.describeContents() ?: 0) or (b?.describeContents() ?: 0) or (c?.describeContents() ?: 0)
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(a, flags)
        dest.writeParcelable(b, flags)
        dest.writeParcelable(c, flags)
    }

    inline operator fun component1(): A = a
    inline operator fun component2(): B = b
    inline operator fun component3(): C = c

    private companion object {
        @JvmField val CREATOR : Parcelable.Creator<ParcelTriple<*, *, *>> = object : Parcelable.Creator<ParcelTriple<*, *, *>> {
            override fun createFromParcel(source: Parcel): ParcelTriple<*, *, *> =
                ParcelTriple::class.java.classLoader.let { cl ->
                    ParcelTriple(
                        source.readParcelable<Parcelable>(cl),
                        source.readParcelable<Parcelable>(cl),
                        source.readParcelable<Parcelable>(cl)
                    )
                }
            override fun newArray(size: Int): Array<ParcelTriple<*, *, *>?> = arrayOfNulls(size)
        }
    }
}

/**
 * Reads/writes a [Serializable] instance from/to [Parcel].
 * Useful for [Enum]s.
 */
class ParcelSerializable<out T : Serializable>(@JvmField val value: T) : BoringParcelable() {
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeSerializable(value)
    inline operator fun component1(): T = value
    private companion object {
        @JvmField val CREATOR : Parcelable.Creator<ParcelSerializable<*>> =
            object : Parcelable.Creator<ParcelSerializable<*>> {
                override fun createFromParcel(source: Parcel): ParcelSerializable<*> =
                    ParcelSerializable(source.readSerializable()!!)
                override fun newArray(size: Int): Array<ParcelSerializable<*>?> =
                    arrayOfNulls(size)
            }
    }
}
