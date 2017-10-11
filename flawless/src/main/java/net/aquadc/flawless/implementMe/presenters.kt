package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpDialogFragmentV4
import net.aquadc.flawless.androidView.MvpFragmentV4

typealias V4FragPresenter<ARG, RET> = Presenter<ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View>

typealias V4DialogFragPresenter<ARG, RET> = Presenter<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog>
