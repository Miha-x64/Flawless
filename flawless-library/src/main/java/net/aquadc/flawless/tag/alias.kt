package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit


typealias SupportFragScreenTag<ARG, RET, SCR> = ScreenTag<ARG, RET, SupportFragment<ARG, RET>, ViewGroup?, View, SCR>
typealias ConsumerSupportFragScreenTag<ARG, SCR> = SupportFragScreenTag<ARG, ParcelUnit, SCR>
typealias SupplierSupportFragScreenTag<RET, SCR> = SupportFragScreenTag<ParcelUnit, RET, SCR>
typealias ActionSupportFragScreenTag<SCR> = SupportFragScreenTag<ParcelUnit, ParcelUnit, SCR>

typealias SupportDialogFragScreenTag<ARG, RET, SCR> = ScreenTag<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, SCR>
typealias ConsumerSupportDialogFragScreenTag<ARG, SCR> = SupportDialogFragScreenTag<ARG, ParcelUnit, SCR>
typealias SupplierSupportDialogFragScreenTag<RET, SCR> = SupportDialogFragScreenTag<ParcelUnit, RET, SCR>
typealias ActionSupportDialogFragScreenTag<SCR> = SupportDialogFragScreenTag<ParcelUnit, ParcelUnit, SCR>

typealias SupportBottomSheetDialogFragScreenTag<ARG, RET, SCR> = ScreenTag<ARG, RET, SupportBottomSheetDialogFragment<ARG, RET>, Context, View, SCR>
typealias ConsumerSupportBottomSheetDialogFragScreenTag<ARG, SCR> = SupportBottomSheetDialogFragScreenTag<ARG, ParcelUnit, SCR>
typealias SupplierSupportBottomSheetDialogFragScreenTag<RET, SCR> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, RET, SCR>
typealias ActionSupportBottomSheetDialogFragScreenTag<SCR> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, ParcelUnit, SCR>
