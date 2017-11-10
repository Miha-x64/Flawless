@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupplierSupportBottomSheetDialogFragPresenterTag
import net.aquadc.flawless.tag.SupplierSupportDialogFragPresenterTag
import net.aquadc.flawless.tag.SupplierSupportFragPresenterTag

inline fun <RET : Parcelable> SupportFragment(
        tag: SupplierSupportFragPresenterTag<RET, *>
) =
        SupplierSupportFragment(tag, ParcelUnit)

inline fun <RET : Parcelable> SupportDialogFragment(
        tag: SupplierSupportDialogFragPresenterTag<RET, *>
) =
        SupplierSupportDialogFragment(tag, ParcelUnit)

inline fun <RET : Parcelable> SupportBottomSheetDialogFragment(
        tag: SupplierSupportBottomSheetDialogFragPresenterTag<RET, *>
) =
        SupplierSupportBottomSheetDialogFragment(tag, ParcelUnit)
