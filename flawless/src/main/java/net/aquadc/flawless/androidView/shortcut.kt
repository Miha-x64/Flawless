@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.V4DialogFragPresenterTag
import net.aquadc.flawless.tag.V4FragPresenterTag

inline fun <RET : Parcelable> MvpV4Fragment(
        tag: V4FragPresenterTag<ParcelUnit, RET, *>
) =
        MvpV4Fragment(tag, ParcelUnit)

inline fun <RET : Parcelable> MvpV4DialogFragment(
        tag: V4DialogFragPresenterTag<ParcelUnit, RET, *>
) =
        MvpV4DialogFragment(tag, ParcelUnit)
