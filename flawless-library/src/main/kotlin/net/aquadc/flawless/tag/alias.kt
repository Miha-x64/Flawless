package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit


typealias SupportFragScreenTag<ARG, RET, STATE> = ScreenTag<ARG, RET, SupportFragment, ViewGroup, View, STATE>
typealias ConsumerSupportFragScreenTag<ARG, STATE> = SupportFragScreenTag<ARG, ParcelUnit, STATE>
typealias SupplierSupportFragScreenTag<RET, STATE> = SupportFragScreenTag<ParcelUnit, RET, STATE>
typealias ActionSupportFragScreenTag<STATE> = SupportFragScreenTag<ParcelUnit, ParcelUnit, STATE>

typealias SupportDialogFragScreenTag<ARG, RET, STATE> = ScreenTag<ARG, RET, SupportDialogFragment, Context, Dialog, STATE>
typealias ConsumerSupportDialogFragScreenTag<ARG, STATE> = SupportDialogFragScreenTag<ARG, ParcelUnit, STATE>
typealias SupplierSupportDialogFragScreenTag<RET, STATE> = SupportDialogFragScreenTag<ParcelUnit, RET, STATE>
typealias ActionSupportDialogFragScreenTag<STATE> = SupportDialogFragScreenTag<ParcelUnit, ParcelUnit, STATE>

typealias SupportBottomSheetDialogFragScreenTag<ARG, RET, STATE> = ScreenTag<ARG, RET, SupportBottomSheetDialogFragment, Context, View, STATE>
typealias ConsumerSupportBottomSheetDialogFragScreenTag<ARG, STATE> = SupportBottomSheetDialogFragScreenTag<ARG, ParcelUnit, STATE>
typealias SupplierSupportBottomSheetDialogFragScreenTag<RET, STATE> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, RET, STATE>
typealias ActionSupportBottomSheetDialogFragScreenTag<STATE> = SupportBottomSheetDialogFragScreenTag<ParcelUnit, ParcelUnit, STATE>
