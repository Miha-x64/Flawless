package net.aquadc.flawless.androidView

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupplierSupportFragPresenterTag


@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupportFragment<ARG, RET>", "net.aquadc.flawless.androidView.SupportFragment"))
typealias MvpV4Fragment<ARG, RET> = SupportFragment<ARG, RET>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ConsumerSupportFragment<ARG>", "net.aquadc.flawless.androidView.ConsumerSupportFragment"))
typealias ConsumerMvpV4Fragment<ARG> = ConsumerSupportFragment<ARG>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupplierSupportFragment<RET>", "net.aquadc.flawless.androidView.SupplierSupportFragment"))
typealias SupplierMvpV4Fragment<RET> = SupplierSupportFragment<RET>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ActionSupportFragment", "net.aquadc.flawless.androidView.ActionSupportFragment"))
typealias ActionMvpV4Fragment = ActionSupportFragment


@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupportFragment(tag)", "net.aquadc.flawless.androidView.SupportFragment"))
fun <RET : Parcelable> MvpV4Fragment(
        tag: SupplierSupportFragPresenterTag<RET, *>
) =
        SupportFragment(tag, ParcelUnit)
