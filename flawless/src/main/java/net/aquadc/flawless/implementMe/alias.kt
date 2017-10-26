package net.aquadc.flawless.implementMe

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelUnit

typealias V4FragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, MvpV4Fragment<ARG>, ViewGroup?, View, STATE>
typealias ConsumerV4FragPresenter<ARG, STATE> = V4FragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierV4FragPresenter<RET, STATE> = V4FragPresenter<ParcelUnit, RET, STATE>
typealias ActionV4FragPresenter<STATE> = V4FragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessV4FragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, MvpV4Fragment<ARG>, ViewGroup?, View>
typealias StatelessConsumerV4FragPresenter<ARG> = StatelessV4FragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierV4FragPresenter<RET> = StatelessV4FragPresenter<ParcelUnit, RET>
typealias StatelessActionV4FragPresenter = StatelessV4FragPresenter<ParcelUnit, ParcelUnit>

typealias V4DialogFragPresenter<ARG, RET, STATE> = Presenter<ARG, RET, MvpV4DialogFragment<ARG>, Context, Dialog, STATE>
typealias ConsumerV4DialogFragPresenter<ARG, STATE> = V4DialogFragPresenter<ARG, ParcelUnit, STATE>
typealias SupplierV4DialogFragPresenter<RET, STATE> = V4DialogFragPresenter<ParcelUnit, RET, STATE>
typealias ActionV4DialogFragPresenter<STATE> = V4DialogFragPresenter<ParcelUnit, ParcelUnit, STATE>

typealias StatelessV4DialogFragPresenter<ARG, RET> = StatelessPresenter<ARG, RET, MvpV4DialogFragment<ARG>, Context, Dialog>
typealias StatelessConsumerV4DialogFragPresenter<ARG> = StatelessV4DialogFragPresenter<ARG, ParcelUnit>
typealias StatelessSupplierV4DialogFragPresenter<RET> = StatelessV4DialogFragPresenter<ParcelUnit, RET>
typealias StatelessActionV4DialogFragPresenter = StatelessV4DialogFragPresenter<ParcelUnit, ParcelUnit>
