package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias V4FragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, MvpV4Fragment<ARG, RET>, ViewGroup?, View, STATE>
typealias ConsumerV4FragPresenter<ARG, STATE> = V4FragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierV4FragPresenter<RET, STATE> = V4FragPresenter<ParcelUnit, RET, STATE>
typealias ActionV4FragPresenter<STATE> = V4FragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessV4FragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, MvpV4Fragment<ARG, RET>, ViewGroup?, View>
typealias StatelessConsumerV4FragPresenter<ARG> = StatelessV4FragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierV4FragPresenter<RET> = StatelessV4FragPresenter<ParcelUnit, RET>
typealias StatelessActionV4FragPresenter = StatelessV4FragPresenter<ParcelUnit, ParcelUnit>

typealias SupportDialogFragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, STATE>
typealias ConsumerSupportDialogFragPresenter<ARG, STATE> = SupportDialogFragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierSupportDialogFragPresenter<RET, STATE> = SupportDialogFragPresenter<ParcelUnit, RET, STATE>
typealias ActionSupportDialogFragPresenter<STATE> = SupportDialogFragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportDialogFragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog>
typealias StatelessConsumerSupportDialogFragPresenter<ARG> = StatelessSupportDialogFragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierSupportDialogFragPresenter<RET> = StatelessSupportDialogFragPresenter<ParcelUnit, RET>
typealias StatelessActionSupportDialogFragPresenter = StatelessSupportDialogFragPresenter<ParcelUnit, ParcelUnit>
