package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias V4FragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpV4Fragment<ARG, RET>, ViewGroup?, View, PRESENTER>

typealias ConsumerV4FragPresenterTag<ARG, PRESENTER> = V4FragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierV4FragPresenterTag<RET, PRESENTER> = V4FragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionV4FragPresenterTag<PRESENTER> = V4FragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>


typealias SupportDialogFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, PRESENTER>

typealias ConsumerSupportDialogFragPresenterTag<ARG, PRESENTER> = SupportDialogFragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierSupportDialogFragPresenterTag<RET, PRESENTER> = SupportDialogFragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionSupportDialogFragPresenterTag<PRESENTER> = SupportDialogFragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>
