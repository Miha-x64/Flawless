package net.aquadc.flawless.implementMe

import kotlin.DeprecationLevel.ERROR


// alias

@Deprecated("", ReplaceWith("AnyScreen"), ERROR)
typealias AnyPresenter = AnyScreen

@Deprecated("", ReplaceWith("SupportFragScreen<ARG, RET, STATE>"), ERROR)
typealias SupportFragPresenter<ARG, RET, STATE> = SupportFragScreen<ARG, RET, STATE>
@Deprecated("", ReplaceWith("ConsumerSupportFragScreen<ARG, STATE>"), ERROR)
typealias ConsumerSupportFragPresenter<ARG, STATE> = ConsumerSupportFragScreen<ARG, STATE>
@Deprecated("", ReplaceWith("SupplierSupportFragScreen<RET, STATE>"), ERROR)
typealias SupplierSupportFragPresenter<RET, STATE> = SupplierSupportFragScreen<RET, STATE>
@Deprecated("", ReplaceWith("ActionSupportFragScreen<STATE>"), ERROR)
typealias ActionSupportFragPresenter<STATE> = ActionSupportFragScreen<STATE>

@Deprecated("", ReplaceWith("StatelessSupportFragScreen<ARG, RET>"), ERROR)
typealias StatelessSupportFragPresenter<ARG, RET> = StatelessSupportFragScreen<ARG, RET>
@Deprecated("", ReplaceWith("StatelessConsumerSupportFragScreen<ARG>"), ERROR)
typealias StatelessConsumerSupportFragPresenter<ARG> = StatelessConsumerSupportFragScreen<ARG>
@Deprecated("", ReplaceWith("StatelessSupplierSupportFragScreen<RET>"), ERROR)
typealias StatelessSupplierSupportFragPresenter<RET> = StatelessSupplierSupportFragScreen<RET>
@Deprecated("", ReplaceWith("StatelessActionSupportFragScreen"), ERROR)
typealias StatelessActionSupportFragPresenter = StatelessActionSupportFragScreen

@Deprecated("", ReplaceWith("SupportDialogFragScreen<ARG, RET, STATE>"), ERROR)
typealias SupportDialogFragPresenter<ARG, RET, STATE> = SupportDialogFragScreen<ARG, RET, STATE>
@Deprecated("", ReplaceWith("ConsumerSupportDialogFragScreen<ARG, STATE>"), ERROR)
typealias ConsumerSupportDialogFragPresenter<ARG, STATE> = ConsumerSupportDialogFragScreen<ARG, STATE>
@Deprecated("", ReplaceWith(" SupplierSupportDialogFragScreen<RET, STATE>"), ERROR)
typealias SupplierSupportDialogFragPresenter<RET, STATE> = SupplierSupportDialogFragScreen<RET, STATE>
@Deprecated("", ReplaceWith("ActionSupportDialogFragScreen<STATE>"), ERROR)
typealias ActionSupportDialogFragPresenter<STATE> = ActionSupportDialogFragScreen<STATE>

@Deprecated("", ReplaceWith("StatelessSupportDialogFragScreen<ARG, RET>"), ERROR)
typealias StatelessSupportDialogFragPresenter<ARG, RET> = StatelessSupportDialogFragScreen<ARG, RET>
@Deprecated("", ReplaceWith("StatelessConsumerSupportDialogFragScreen<ARG>"), ERROR)
typealias StatelessConsumerSupportDialogFragPresenter<ARG> = StatelessConsumerSupportDialogFragScreen<ARG>
@Deprecated("", ReplaceWith("StatelessSupplierSupportDialogFragScreen<RET>"), ERROR)
typealias StatelessSupplierSupportDialogFragPresenter<RET> = StatelessSupplierSupportDialogFragScreen<RET>
@Deprecated("", ReplaceWith("StatelessActionSupportDialogFragScreen"), ERROR)
typealias StatelessActionSupportDialogFragPresenter = StatelessActionSupportDialogFragScreen

@Deprecated("", ReplaceWith("SupportBottomSheetDialogFragScreen<ARG, RET, STATE>"), ERROR)
typealias SupportBottomSheetDialogFragPresenter<ARG, RET, STATE> = SupportBottomSheetDialogFragScreen<ARG, RET, STATE>
@Deprecated("", ReplaceWith("ConsumerBottomSheetSupportDialogFragScreen<ARG, STATE>"), ERROR)
typealias ConsumerBottomSheetSupportDialogFragPresenter<ARG, STATE> = ConsumerBottomSheetSupportDialogFragScreen<ARG, STATE>
@Deprecated("", ReplaceWith("SupplierBottomSheetSupportDialogFragScreen<RET, STATE>"), ERROR)
typealias SupplierBottomSheetSupportDialogFragPresenter<RET, STATE> = SupplierBottomSheetSupportDialogFragScreen<RET, STATE>
@Deprecated("", ReplaceWith("ActionBottomSheetSupportDialogFragScreen<STATE>"), ERROR)
typealias ActionBottomSheetSupportDialogFragPresenter<STATE> = ActionBottomSheetSupportDialogFragScreen<STATE>

@Deprecated("", ReplaceWith("StatelessSupportBottomSheetDialogFragScreen<ARG, RET>"), ERROR)
typealias StatelessSupportBottomSheetDialogFragPresenter<ARG, RET> = StatelessSupportBottomSheetDialogFragScreen<ARG, RET>
@Deprecated("", ReplaceWith("StatelessConsumerBottomSheetSupportDialogFragScreen<ARG>"), ERROR)
typealias StatelessConsumerBottomSheetSupportDialogFragPresenter<ARG> = StatelessConsumerBottomSheetSupportDialogFragScreen<ARG>
@Deprecated("", ReplaceWith("StatelessSupplierSupportBottomSheetDialogFragScreen<RET>"), ERROR)
typealias StatelessSupplierSupportBottomSheetDialogFragPresenter<RET> = StatelessSupplierSupportBottomSheetDialogFragScreen<RET>
@Deprecated("", ReplaceWith("StatelessActionSupportBottomSheetDialogFragScreen"), ERROR)
typealias StatelessActionSupportBottomSheetDialogFragPresenter = StatelessActionSupportBottomSheetDialogFragScreen

// Screen

@Deprecated("", ReplaceWith("Screen<ARG, RET, HOST, PARENT, VIEW, STATE>"), ERROR)
typealias Presenter<ARG, RET, HOST, PARENT, VIEW, STATE> = Screen<ARG, RET, HOST, PARENT, VIEW, STATE>

// ScreenFactory

@Deprecated("", ReplaceWith("ScreenFactory"), ERROR)
typealias PresenterFactory = ScreenFactory

// StatelessScreen

@Deprecated("", ReplaceWith("StatelessScreen<ARG, RET, HOST, PARENT, VIEW>"), ERROR)
typealias StatelessPresenter<ARG, RET, HOST, PARENT, VIEW> = StatelessScreen<ARG, RET, HOST, PARENT, VIEW>
