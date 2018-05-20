package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit


typealias SupportFragScreenTag<ARG, RET, STATE, SCR> = ScreenTag<ARG, RET, SupportFragment, ViewGroup, View, STATE, SCR>
typealias ConsumerSupportFragScreenTag<ARG, STATE, SCR> = SupportFragScreenTag<ARG, ParcelUnit, STATE, SCR>
typealias SupplierSupportFragScreenTag<RET, STATE, SCR> = SupportFragScreenTag<ParcelUnit, RET, STATE, SCR>
typealias ActionSupportFragScreenTag<STATE, SCR> = SupportFragScreenTag<ParcelUnit, ParcelUnit, STATE, SCR>

typealias SupportDialogFragScreenTag<ARG, RET, STATE, SCR> = ScreenTag<ARG, RET, SupportDialogFragment, Context, Dialog, STATE, SCR>
typealias ConsumerSupportDialogFragScreenTag<ARG, STATE, SCR> = SupportDialogFragScreenTag<ARG, ParcelUnit, STATE, SCR>
typealias SupplierSupportDialogFragScreenTag<RET, STATE, SCR> = SupportDialogFragScreenTag<ParcelUnit, RET, STATE, SCR>
typealias ActionSupportDialogFragScreenTag<STATE, SCR> = SupportDialogFragScreenTag<ParcelUnit, ParcelUnit, STATE, SCR>

typealias SupportBottomSheetDialogFragScreenTag<ARG, RET, STATE, SCR> = ScreenTag<ARG, RET, SupportBottomSheetDialogFragment, Context, View, STATE, SCR>
typealias ConsumerSupportBottomSheetDialogFragScreenTag<ARG, STATE, SCR> = SupportBottomSheetDialogFragScreenTag<ARG, ParcelUnit, STATE, SCR>
typealias SupplierSupportBottomSheetDialogFragScreenTag<RET, STATE, SCR> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, RET, STATE, SCR>
typealias ActionSupportBottomSheetDialogFragScreenTag<STATE, SCR> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, ParcelUnit, STATE, SCR>
