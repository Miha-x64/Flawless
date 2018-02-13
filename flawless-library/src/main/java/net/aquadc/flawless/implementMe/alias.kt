package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias AnyScreen = Screen<*, *, *, *, *, *>

typealias SupportFragScreen<ARG, RET, STATE> = Screen<ARG, RET, SupportFragment<ARG, RET>, ViewGroup, View, STATE>
typealias ConsumerSupportFragScreen<ARG, STATE> = SupportFragScreen<ARG, ParcelUnit, STATE>
typealias SupplierSupportFragScreen<RET, STATE> = SupportFragScreen<ParcelUnit, RET, STATE>
typealias ActionSupportFragScreen<STATE> = SupportFragScreen<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportFragScreen<ARG, RET> = StatelessScreen<ARG, RET, SupportFragment<ARG, RET>, ViewGroup, View>
typealias StatelessConsumerSupportFragScreen<ARG> = StatelessSupportFragScreen<ARG, ParcelUnit>
typealias StatelessSupplierSupportFragScreen<RET> = StatelessSupportFragScreen<ParcelUnit, RET>
typealias StatelessActionSupportFragScreen = StatelessSupportFragScreen<ParcelUnit, ParcelUnit>


typealias SupportDialogFragScreen<ARG, RET, STATE> = Screen<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, STATE>
typealias ConsumerSupportDialogFragScreen<ARG, STATE> = SupportDialogFragScreen<ARG, ParcelUnit, STATE>
typealias SupplierSupportDialogFragScreen<RET, STATE> = SupportDialogFragScreen<ParcelUnit, RET, STATE>
typealias ActionSupportDialogFragScreen<STATE> = SupportDialogFragScreen<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportDialogFragScreen<ARG, RET> = StatelessScreen<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog>
typealias StatelessConsumerSupportDialogFragScreen<ARG> = StatelessSupportDialogFragScreen<ARG, ParcelUnit>
typealias StatelessSupplierSupportDialogFragScreen<RET> = StatelessSupportDialogFragScreen<ParcelUnit, RET>
typealias StatelessActionSupportDialogFragScreen = StatelessSupportDialogFragScreen<ParcelUnit, ParcelUnit>


typealias SupportBottomSheetDialogFragScreen<ARG, RET, STATE> = Screen<ARG, RET, SupportBottomSheetDialogFragment<ARG, RET>, Context, View, STATE>
typealias ConsumerBottomSheetSupportDialogFragScreen<ARG, STATE> = SupportBottomSheetDialogFragScreen<ARG, ParcelUnit, STATE>
typealias SupplierBottomSheetSupportDialogFragScreen<RET, STATE> = SupportBottomSheetDialogFragScreen<ParcelUnit, RET, STATE>
typealias ActionBottomSheetSupportDialogFragScreen<STATE> = SupportBottomSheetDialogFragScreen<ParcelUnit, ParcelUnit, STATE>

typealias StatelessSupportBottomSheetDialogFragScreen<ARG, RET> = StatelessScreen<ARG, RET, SupportBottomSheetDialogFragment<ARG, RET>, Context, View>
typealias StatelessConsumerBottomSheetSupportDialogFragScreen<ARG> = StatelessSupportBottomSheetDialogFragScreen<ARG, ParcelUnit>
typealias StatelessSupplierSupportBottomSheetDialogFragScreen<RET> = StatelessSupportBottomSheetDialogFragScreen<ParcelUnit, RET>
typealias StatelessActionSupportBottomSheetDialogFragScreen = StatelessSupportBottomSheetDialogFragScreen<ParcelUnit, ParcelUnit>
