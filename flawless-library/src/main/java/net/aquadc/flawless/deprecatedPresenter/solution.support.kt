package net.aquadc.flawless.solution.support

import kotlin.DeprecationLevel.ERROR


@Deprecated("", ReplaceWith("ConfirmationDialogScreen"), ERROR)
typealias ConfirmationDialogPresenter = ConfirmationDialogScreen

@Deprecated("", ReplaceWith("LoadingDialogScreen<ARG, LR_RET>"), ERROR)
typealias LoadingDialogPresenter<ARG, LR_RET> = LoadingDialogScreen<ARG, LR_RET>
