package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias SupportFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, SupportFragment<ARG, RET>, ViewGroup?, View, PRESENTER>

typealias ConsumerSupportFragPresenterTag<ARG, PRESENTER> = SupportFragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierSupportFragPresenterTag<RET, PRESENTER> = SupportFragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionSupportFragPresenterTag<PRESENTER> = SupportFragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>


typealias SupportDialogFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, PRESENTER>

typealias ConsumerSupportDialogFragPresenterTag<ARG, PRESENTER> = SupportDialogFragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierSupportDialogFragPresenterTag<RET, PRESENTER> = SupportDialogFragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionSupportDialogFragPresenterTag<PRESENTER> = SupportDialogFragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>
