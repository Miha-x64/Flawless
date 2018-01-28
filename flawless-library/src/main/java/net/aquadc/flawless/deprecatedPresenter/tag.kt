package net.aquadc.flawless.tag

import kotlin.DeprecationLevel.ERROR


// alias

@Deprecated("", ReplaceWith("SupportFragScreenTag<ARG, RET, SCR>"), ERROR)
typealias SupportFragPresenterTag<ARG, RET, SCR> = SupportFragScreenTag<ARG, RET, SCR>
@Deprecated("", ReplaceWith("ConsumerSupportFragScreenTag<ARG, SCR>"), ERROR)
typealias ConsumerSupportFragPresenterTag<ARG, SCR> = ConsumerSupportFragScreenTag<ARG, SCR>
@Deprecated("", ReplaceWith("SupplierSupportFragScreenTag<RET, SCR>"), ERROR)
typealias SupplierSupportFragPresenterTag<RET, SCR> = SupplierSupportFragScreenTag<RET, SCR>
@Deprecated("", ReplaceWith("ActionSupportFragScreenTag<SCR>"), ERROR)
typealias ActionSupportFragPresenterTag<SCR> = ActionSupportFragScreenTag<SCR>

@Deprecated("", ReplaceWith("SupportDialogFragScreenTag<ARG, RET, SCR>"), ERROR)
typealias SupportDialogFragPresenterTag<ARG, RET, SCR> = SupportDialogFragScreenTag<ARG, RET, SCR>
@Deprecated("", ReplaceWith("ConsumerSupportDialogFragScreenTag<ARG, SCR>"), ERROR)
typealias ConsumerSupportDialogFragPresenterTag<ARG, SCR> = ConsumerSupportDialogFragScreenTag<ARG, SCR>
@Deprecated("", ReplaceWith("SupplierSupportDialogFragScreenTag<RET, SCR>"), ERROR)
typealias SupplierSupportDialogFragPresenterTag<RET, SCR> = SupplierSupportDialogFragScreenTag<RET, SCR>
@Deprecated("", ReplaceWith("ActionSupportDialogFragScreenTag<SCR>"), ERROR)
typealias ActionSupportDialogFragPresenterTag<SCR> = ActionSupportDialogFragScreenTag<SCR>

@Deprecated("", ReplaceWith("SupportBottomSheetDialogFragScreenTag<ARG, RET, SCR>"), ERROR)
typealias SupportBottomSheetDialogFragPresenterTag<ARG, RET, SCR> = SupportBottomSheetDialogFragScreenTag<ARG, RET, SCR>
@Deprecated("", ReplaceWith("ConsumerSupportBottomSheetDialogFragScreenTag<ARG, SCR>"), ERROR)
typealias ConsumerSupportBottomSheetDialogFragPresenterTag<ARG, SCR> = ConsumerSupportBottomSheetDialogFragScreenTag<ARG, SCR>
@Deprecated("", ReplaceWith("SupplierSupportBottomSheetDialogFragScreenTag<RET, SCR>"), ERROR)
typealias SupplierSupportBottomSheetDialogFragPresenterTag<RET, SCR> = SupplierSupportBottomSheetDialogFragScreenTag<RET, SCR>
@Deprecated("", ReplaceWith("ActionSupportBottomSheetDialogFragScreenTag<SCR>"), ERROR)
typealias ActionSupportBottomSheetDialogFragPresenterTag<SCR> = ActionSupportBottomSheetDialogFragScreenTag<SCR>

// ScreenTag

@Deprecated("", ReplaceWith("ScreenTag<ARG, RET, HOST, PARENT, VIEW, SCR>"), ERROR)
typealias PresenterTag<ARG, RET, HOST, PARENT, VIEW, SCR> = ScreenTag<ARG, RET, HOST, PARENT, VIEW, SCR>


