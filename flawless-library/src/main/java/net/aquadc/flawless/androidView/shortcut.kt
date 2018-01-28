@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.androidView

import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Screen
import net.aquadc.flawless.parcel.NoOpParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.*


inline fun <RET : Parcelable> SupportFragment(
        tag: SupplierSupportFragScreenTag<RET, *>
) =
        SupplierSupportFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportFragment(
        tag: SupportFragScreenTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportFragment(
        tag: SupplierSupportFragScreenTag<RET, *>,
        target: HOST,
        requestCode: Int, resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }



inline fun <RET : Parcelable> SupportDialogFragment(
        tag: SupplierSupportDialogFragScreenTag<RET, *>
) =
        SupplierSupportDialogFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportDialogFragment(
        tag: SupportDialogFragScreenTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportDialogFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportDialogFragment(
        tag: SupplierSupportDialogFragScreenTag<RET, *>,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportDialogFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }



inline fun <RET : Parcelable> SupportBottomSheetDialogFragment(
        tag: SupplierSupportBottomSheetDialogFragScreenTag<RET, *>
) =
        SupplierSupportBottomSheetDialogFragment(tag, ParcelUnit)

inline fun <HOST, ARG : Parcelable, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportBottomSheetDialogFragment(
        tag: SupportBottomSheetDialogFragScreenTag<ARG, RET, *>, arg: ARG,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportBottomSheetDialogFragment(tag, arg)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }

inline fun <HOST, RET : Parcelable, SCR : Screen<*, *, HOST, *, *, *>>
        SCR.SupportBottomSheetDialogFragment(
        tag: SupplierSupportBottomSheetDialogFragScreenTag<RET, *>,
        target: HOST, requestCode: Int,
        resultCallback: ParcelFunction2<SCR, RET, Unit>,
        cancellationCallback: ParcelFunction1<SCR, Unit> = NoOpParcelFunction1
) where HOST : Host, HOST : Fragment =
        SupportBottomSheetDialogFragment(tag, ParcelUnit)
                .also {
                    it.setTargetFragment(target, requestCode)
                    target.exchange.registerResultCallback(requestCode, resultCallback, cancellationCallback)
                }
