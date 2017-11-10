@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupplierSupportDialogFragPresenterTag
import net.aquadc.flawless.tag.SupplierV4FragPresenterTag

inline fun <RET : Parcelable> MvpV4Fragment(
        tag: SupplierV4FragPresenterTag<RET, *>
) =
        SupplierMvpV4Fragment(tag, ParcelUnit)

inline fun <RET : Parcelable> SupportDialogFragment(
        tag: SupplierSupportDialogFragPresenterTag<RET, *>
) =
        SupplierSupportDialogFragment(tag, ParcelUnit)
