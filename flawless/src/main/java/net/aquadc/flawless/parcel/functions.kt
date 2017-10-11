package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable
import java.lang.reflect.Modifier

inline fun <T, R> pureParcelFunction(crossinline function: (T) -> R): ParcelFunction<T, R> =
        object : ParcelableFuncImpl<T, R>() {
            override fun invoke(p1: T): R = function(p1)
        }

inline fun <T1, T2, R> pureParcelFunction2(crossinline function: (T1, T2) -> R): ParcelFunction2<T1, T2, R> =
        object : ParcelableFunc2Impl<T1, T2, R>() {
            override fun invoke(p1: T1, p2: T2): R = function(p1, p2)
        }

interface ParcelFunction<in T, out R> : (T) -> R, Parcelable

interface ParcelFunction2<in T1, in T2, out R> : (T1, T2) -> R, Parcelable

@PublishedApi
internal abstract class ParcelableFuncImpl<in T, out R> protected constructor() : ParcelFunction<T, R> {

    init { assertCorrectConstructor(javaClass) }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(javaClass.name)
    }

    companion object CREATOR : Parcelable.Creator<ParcelableFuncImpl<*, *>> {
        override fun createFromParcel(source: Parcel): ParcelableFuncImpl<*, *> =
                Class.forName(source.readString(), true, ParcelableFuncImpl::class.java.classLoader).newInstance()
                        as ParcelableFuncImpl<*, *>

        override fun newArray(size: Int): Array<ParcelableFuncImpl<*, *>?> =
                arrayOfNulls(size)
    }
}

@PublishedApi
internal abstract class ParcelableFunc2Impl<in T1, in T2, out R> protected constructor() : ParcelFunction2<T1, T2, R> {

    init { assertCorrectConstructor(javaClass) }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(javaClass.name)
    }

    companion object CREATOR : Parcelable.Creator<ParcelableFunc2Impl<*, *, *>> {
        override fun createFromParcel(source: Parcel): ParcelableFunc2Impl<*, *, *> =
                Class.forName(source.readString(), true, ParcelableFunc2Impl::class.java.classLoader).newInstance()
                        as ParcelableFunc2Impl<*, *, *>

        override fun newArray(size: Int): Array<ParcelableFunc2Impl<*, *, *>?> =
                arrayOfNulls(size)
    }
}

private fun assertCorrectConstructor(klass: Class<*>) {
    val ctors = klass.constructors
    if (ctors.size != 1)
        throw AssertionError("ParcelableFunc must have the only public constructor()")

    val ctor = ctors[0]
    if ((ctor.modifiers and Modifier.PUBLIC) != Modifier.PUBLIC)
        throw AssertionError("ParcelableFunc's constructor must be public")

    val parameterTypes = ctor.parameterTypes
    if (parameterTypes != null && parameterTypes.isNotEmpty())
        throw AssertionError("ParcelableFunc's constructor must accept no arguments, got $ctor")
}
