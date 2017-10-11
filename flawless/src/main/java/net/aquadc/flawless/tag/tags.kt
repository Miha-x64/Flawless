package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpDialogFragmentV4
import net.aquadc.flawless.androidView.MvpFragmentV4
import kotlin.reflect.KProperty


typealias V4FragPresenterTag<ARG, RET> = PresenterTag<ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View>

fun <ARG : Parcelable, RET : Parcelable> v4FragPresenterTag(
): PresenterDelegateProvider<ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View> = tag()


typealias V4DialogFragPresenterTag<ARG, RET> = PresenterTag<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog>

fun <ARG : Parcelable, RET : Parcelable> v4DialogFragPresenterTag(
): PresenterDelegateProvider<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog> = tag()


@Suppress("UNCHECKED_CAST") // it's safe here, because tag doesn't store anything type-dependent
fun <ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> tag(
) =
        UncheckedPresenterDelegateProvider as PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW>

interface PresenterDelegateProvider<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): PresenterTag<ARG, RET, HOST, PARENT, VIEW>
}

private object UncheckedPresenterDelegateProvider : PresenterDelegateProvider<Parcelable, Parcelable, Any, Any, Any> {
    override fun provideDelegate(thisRef: Any, prop: KProperty<*>) =
            PresenterTag<Parcelable, Parcelable, Any, Any, Any>(prop.name)
}
