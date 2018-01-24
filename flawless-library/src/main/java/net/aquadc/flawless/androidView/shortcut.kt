@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.*


inline fun <RET : Parcelable> SupportFragment(
        tag: SupplierSupportFragPresenterTag<RET, *>
) =
        SupplierSupportFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportFragment(
        tag: SupportFragPresenterTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportFragment(
        tag: SupplierSupportFragPresenterTag<RET, *>,
        target: HOST,
        requestCode: Int, resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }



inline fun <RET : Parcelable> SupportDialogFragment(
        tag: SupplierSupportDialogFragPresenterTag<RET, *>
) =
        SupplierSupportDialogFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportDialogFragment(
        tag: SupportDialogFragPresenterTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportDialogFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportDialogFragment(
        tag: SupplierSupportDialogFragPresenterTag<RET, *>,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportDialogFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }



inline fun <RET : Parcelable> SupportBottomSheetDialogFragment(
        tag: SupplierSupportBottomSheetDialogFragPresenterTag<RET, *>
) =
        SupplierSupportBottomSheetDialogFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportBottomSheetDialogFragment(
        tag: SupportBottomSheetDialogFragPresenterTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportBottomSheetDialogFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, PRESENTER : Presenter<*, *, HOST, *, *, *>>
        PRESENTER.SupportBottomSheetDialogFragment(
        tag: SupplierSupportBottomSheetDialogFragPresenterTag<RET, *>,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
        cancellationCallback: ParcelFunction1<PRESENTER, Unit> = NoOpParcelFunction1
) where HOST : Host<*>, HOST : Fragment =
        SupportBottomSheetDialogFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }
