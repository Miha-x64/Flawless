package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias V4FragPresenter<ARG, RET> = Presenter<ARG, RET, MvpV4Fragment<ARG>, ViewGroup?, View>
typealias ConsumerV4FragPresenter<ARG> = V4FragPresenter<ARG, ParcelUnit>
typealias SupplierV4FragPresenter<RET> = V4FragPresenter<ParcelUnit, RET>
typealias ActionV4FragPresenter = V4FragPresenter<ParcelUnit, ParcelUnit>

typealias V4DialogFragPresenter<ARG, RET> = Presenter<ARG, RET, MvpV4DialogFragment<ARG>, Context, Dialog>
typealias ConsumerV4DialogFragPresenter<ARG> = V4DialogFragPresenter<ARG, ParcelUnit>
typealias SupplierV4DialogFragPresenter<RET> = V4DialogFragPresenter<ParcelUnit, RET>
typealias ActionV4DialogFragPresenter = V4DialogFragPresenter<ParcelUnit, ParcelUnit>
