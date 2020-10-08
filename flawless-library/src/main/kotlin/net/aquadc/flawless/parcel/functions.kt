package net.aquadc.flawless.parcel

import android.os.Parcel
import android.os.Parcelable
import java.lang.reflect.Modifier

interface ParcelFunction0<out R>
    : () -> R, Parcelable
interface ParcelFunction1<in T, out R>
    : (T) -> R, Parcelable
interface ParcelFunction2<in T1, in T2, out R>
    : (T1, T2) -> R, Parcelable
interface ParcelFunction3<in T1, in T2, in T3, out R>
    : (T1, T2, T3) -> R, Parcelable
interface ParcelFunction4<in T1, in T2, in T3, in T4, out R>
    : (T1, T2, T3, T4) -> R, Parcelable
interface ParcelFunction5<in T1, in T2, in T3, in T4, in T5, out R>
    : (T1, T2, T3, T4, T5) -> R, Parcelable
interface ParcelFunction6<in T1, in T2, in T3, in T4, in T5, in T6, out R>
    : (T1, T2, T3, T4, T5, T6) -> R, Parcelable
interface ParcelFunction7<in T1, in T2, in T3, in T4, in T5, in T6, in T7, out R>
    : (T1, T2, T3, T4, T5, T6, T7) -> R, Parcelable
interface ParcelFunction8<in T1, in T2, in T3, in T4, in T5, in T6, in T7, in T8, out R>
    : (T1, T2, T3, T4, T5, T6, T7, T8) -> R, Parcelable

val NoOpParcelFunction0 = pureParcelFunction { ->  }
val NoOpParcelFunction1 = pureParcelFunction { _: Any? ->  }
val NoOpParcelFunction2 = pureParcelFunction { _: Any?, _: Any? ->  }
val NoOpParcelFunction3 = pureParcelFunction { _: Any?, _: Any?, _:Any? ->  }
val NoOpParcelFunction4 = pureParcelFunction { _: Any?, _: Any?, _:Any?, _:Any? ->  }
val NoOpParcelFunction5 = pureParcelFunction { _: Any?, _: Any?, _:Any?, _:Any?, _:Any? ->  }
val NoOpParcelFunction6 = pureParcelFunction { _: Any?, _: Any?, _:Any?, _:Any?, _:Any?, _:Any? ->  }
val NoOpParcelFunction7 = pureParcelFunction { _: Any?, _: Any?, _:Any?, _:Any?, _:Any?, _:Any?, _:Any? ->  }
val NoOpParcelFunction8 = pureParcelFunction { _: Any?, _: Any?, _:Any?, _:Any?, _:Any?, _:Any?, _:Any?, _:Any? ->  }

// '0' version is useful to fix overload resolution when you want a lambda without arrow
inline fun <R> pureParcelFunction0(crossinline function: () -> R): ParcelFunction0<R> =
    object : ParcelableFunc0Impl<R>() { override fun invoke(): R = function() }
inline fun <R> pureParcelFunction(crossinline function: () -> R): ParcelFunction0<R> =
    object : ParcelableFunc0Impl<R>() {
        override fun invoke(): R = function()
    }

// '1' version is useful to fix overload resolution when you want a lambda with `it` parameter
inline fun <T, R> pureParcelFunction1(crossinline function: (T) -> R): ParcelFunction1<T, R> =
    object : ParcelableFunc1Impl<T, R>() { override fun invoke(p1: T): R = function(p1) }
inline fun <T, R> pureParcelFunction(crossinline function: (T) -> R): ParcelFunction1<T, R> =
    object : ParcelableFunc1Impl<T, R>() {
        override fun invoke(p1: T): R = function(p1)
    }

@Deprecated("renamed", ReplaceWith("pureParcelFunction(function)"))
inline fun <T1, T2, R> pureParcelFunction2(crossinline function: (T1, T2) -> R): ParcelFunction2<T1, T2, R> =
    object : ParcelableFunc2Impl<T1, T2, R>() { override fun invoke(p1: T1, p2: T2): R = function(p1, p2) }
inline fun <T1, T2, R> pureParcelFunction(crossinline function: (T1, T2) -> R): ParcelFunction2<T1, T2, R> =
    object : ParcelableFunc2Impl<T1, T2, R>() {
        override fun invoke(p1: T1, p2: T2): R = function(p1, p2)
    }

@Deprecated("renamed", ReplaceWith("pureParcelFunction(function)"))
inline fun <T1, T2, T3, R> pureParcelFunction3(crossinline function: (T1, T2, T3) -> R): ParcelFunction3<T1, T2, T3, R> =
    object : ParcelableFunc3Impl<T1, T2, T3, R>() { override fun invoke(p1: T1, p2: T2, p3: T3): R = function(p1, p2, p3) }
inline fun <T1, T2, T3, R> pureParcelFunction(crossinline function: (T1, T2, T3) -> R): ParcelFunction3<T1, T2, T3, R> =
    object : ParcelableFunc3Impl<T1, T2, T3, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3): R = function(p1, p2, p3)
    }

inline fun <T1, T2, T3, T4, R> pureParcelFunction(
    crossinline function: (T1, T2, T3, T4) -> R
): ParcelFunction4<T1, T2, T3, T4, R> =
    object : ParcelableFunc4Impl<T1, T2, T3, T4, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3, p4: T4): R = function(p1, p2, p3, p4)
    }

inline fun <T1, T2, T3, T4, T5, R> pureParcelFunction(
    crossinline function: (T1, T2, T3, T4, T5) -> R
): ParcelFunction5<T1, T2, T3, T4, T5, R> =
    object : ParcelableFunc5Impl<T1, T2, T3, T4, T5, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3, p4: T4, p5: T5): R = function(p1, p2, p3, p4, p5)
    }

inline fun <T1, T2, T3, T4, T5, T6, R> pureParcelFunction(
    crossinline function: (T1, T2, T3, T4, T5, T6) -> R
): ParcelFunction6<T1, T2, T3, T4, T5, T6, R> =
    object : ParcelableFunc6Impl<T1, T2, T3, T4, T5, T6, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6): R = function(p1, p2, p3, p4, p5, p6)
    }

inline fun <T1, T2, T3, T4, T5, T6, T7, R> pureParcelFunction(
    crossinline function: (T1, T2, T3, T4, T5, T6, T7) -> R
): ParcelFunction7<T1, T2, T3, T4, T5, T6, T7, R> =
    object : ParcelableFunc7Impl<T1, T2, T3, T4, T5, T6, T7, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7): R =
            function(p1, p2, p3, p4, p5, p6, p7)
    }

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> pureParcelFunction(
    crossinline function: (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): ParcelFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> =
    object : ParcelableFunc8Impl<T1, T2, T3, T4, T5, T6, T7, T8, R>() {
        override fun invoke(p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8): R =
            function(p1, p2, p3, p4, p5, p6, p7, p8)
    }

@PublishedApi internal abstract class ParcelableFunc0Impl<out R>
    : ParcelFuncImpl(), ParcelFunction0<R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc0Impl<*>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc1Impl<in T, out R>
    : ParcelFuncImpl(), ParcelFunction1<T, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc1Impl<*, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc2Impl<in T1, in T2, out R>
    : ParcelFuncImpl(), ParcelFunction2<T1, T2, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc2Impl<*, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc3Impl<in T1, in T2, in T3, out R>
    : ParcelFuncImpl(), ParcelFunction3<T1, T2, T3, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc3Impl<*, *, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc4Impl<in T1, in T2, in T3, in T4, out R>
    : ParcelFuncImpl(), ParcelFunction4<T1, T2, T3, T4, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc4Impl<*, *, *, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc5Impl<in T1, in T2, in T3, in T4, in T5, out R>
    : ParcelFuncImpl(), ParcelFunction5<T1, T2, T3, T4, T5, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc5Impl<*, *, *, *, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc6Impl<in T1, in T2, in T3, in T4, in T5, in T6, out R>
    : ParcelFuncImpl(), ParcelFunction6<T1, T2, T3, T4, T5, T6, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc6Impl<*, *, *, *, *, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc7Impl<in T1, in T2, in T3, in T4, in T5, in T6, in T7, out R>
    : ParcelFuncImpl(), ParcelFunction7<T1, T2, T3, T4, T5, T6, T7, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc7Impl<*, *, *, *, *, *, *, *>>()
    }
}
@PublishedApi internal abstract class ParcelableFunc8Impl<in T1, in T2, in T3, in T4, in T5, in T6, in T7, in T8, out R>
    : ParcelFuncImpl(), ParcelFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
    private companion object {
        @JvmField val CREATOR = parcelFuncCreator<ParcelableFunc8Impl<*, *, *, *, *, *, *, *, *>>()
    }
}

internal abstract class ParcelFuncImpl : Parcelable {
    init {
        val ctors = javaClass.constructors
        if (ctors.size != 1)
            throw AssertionError("ParcelableFunc must have the only public constructor()")

        val ctor = ctors[0]
        if ((ctor.modifiers and Modifier.PUBLIC) != Modifier.PUBLIC)
            throw AssertionError("ParcelableFunc's constructor must be public")

        if (ctor.parameterTypes.isNotEmpty())
            throw AssertionError("ParcelableFunc's constructor must accept no arguments, got $ctor")
    }

    final override fun describeContents(): Int = 0
    final override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeString(javaClass.name)
}
private abstract class ParcelFuncCreator<T> : Parcelable.Creator<T> {
    final override fun createFromParcel(source: Parcel): T =
        Class.forName(source.readString()!!, true, ParcelFuncImpl::class.java.classLoader).newInstance() as T
}
private inline fun <reified T> parcelFuncCreator(): Parcelable.Creator<T> =
    object : ParcelFuncCreator<T>() {
        override fun newArray(size: Int): Array<T> = arrayOfNulls<T>(size) as Array<T>
    }
