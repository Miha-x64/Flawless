package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias SupportFragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, SupportFragment<ARG, RET>, ViewGroup?, View, STATE>
typealias ConsumerSupportFragPresenter<ARG, STATE> = SupportFragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierSupportFragPresenter<RET, STATE> = SupportFragPresenter<ParcelUnit, RET, STATE>
typealias ActionSupportFragPresenter<STATE> = SupportFragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportFragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, SupportFragment<ARG, RET>, ViewGroup?, View>
typealias StatelessConsumerSupportFragPresenter<ARG> = StatelessSupportFragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierSupportFragPresenter<RET> = StatelessSupportFragPresenter<ParcelUnit, RET>
typealias StatelessActionSupportFragPresenter = StatelessSupportFragPresenter<ParcelUnit, ParcelUnit>


typealias SupportDialogFragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, STATE>
typealias ConsumerSupportDialogFragPresenter<ARG, STATE> = SupportDialogFragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierSupportDialogFragPresenter<RET, STATE> = SupportDialogFragPresenter<ParcelUnit, RET, STATE>
typealias ActionSupportDialogFragPresenter<STATE> = SupportDialogFragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportDialogFragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog>
typealias StatelessConsumerSupportDialogFragPresenter<ARG> = StatelessSupportDialogFragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierSupportDialogFragPresenter<RET> = StatelessSupportDialogFragPresenter<ParcelUnit, RET>
typealias StatelessActionSupportDialogFragPresenter = StatelessSupportDialogFragPresenter<ParcelUnit, ParcelUnit>


typealias SupportBottomSheetDialogFragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, SupportBottomSheetDialogFragment<ARG, RET>, Context, View, STATE>
typealias ConsumerBottomSheetSupportDialogFragPresenter<ARG, STATE> = SupportBottomSheetDialogFragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierBottomSheetSupportDialogFragPresenter<RET, STATE> = SupportBottomSheetDialogFragPresenter<ParcelUnit, RET, STATE>
typealias ActionBottomSheetSupportDialogFragPresenter<STATE> = SupportBottomSheetDialogFragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportBottomSheetDialogFragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, SupportBottomSheetDialogFragment<ARG, RET>, Context, View>
typealias StatelessConsumerBottomSheetSupportDialogFragPresenter<ARG> = StatelessSupportBottomSheetDialogFragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierSupportBottomSheetDialogFragPresenter<RET> = StatelessSupportBottomSheetDialogFragPresenter<ParcelUnit, RET>
typealias StatelessActionSupportBottomSheetDialogFragPresenter = StatelessSupportBottomSheetDialogFragPresenter<ParcelUnit, ParcelUnit>
