package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias V4FragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpV4Fragment<ARG>, ViewGroup?, View, PRESENTER>

typealias ConsumerV4FragPresenterTag<ARG, PRESENTER> = V4FragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierV4FragPresenterTag<RET, PRESENTER> = V4FragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionV4FragPresenterTag<PRESENTER> = V4FragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>


typealias V4DialogFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpV4DialogFragment<ARG>, Context, Dialog, PRESENTER>

typealias ConsumerV4DialogFragPresenterTag<ARG, PRESENTER> = V4DialogFragPresenterTag<ARG, ParcelUnit, PRESENTER>
typealias SupplierV4DialogFragPresenterTag<RET, PRESENTER> = V4DialogFragPresenterTag<ParcelUnit, RET, PRESENTER>
typealias ActionV4DialogFragPresenterTag<PRESENTER> = V4DialogFragPresenterTag<ParcelUnit, ParcelUnit, PRESENTER>
