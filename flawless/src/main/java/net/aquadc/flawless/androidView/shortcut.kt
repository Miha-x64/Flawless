@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupplierV4DialogFragPresenterTag
import net.aquadc.flawless.tag.SupplierV4FragPresenterTag

inline fun <RET : Parcelable> MvpV4Fragment(
        tag: SupplierV4FragPresenterTag<RET, *>
) =
        MvpV4Fragment(tag, ParcelUnit)

inline fun <RET : Parcelable> MvpV4DialogFragment(
        tag: SupplierV4DialogFragPresenterTag<RET, *>
) =
        MvpV4DialogFragment(tag, ParcelUnit)
