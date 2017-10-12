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

inline fun <reified ARG : Parcelable, reified RET : Parcelable> v4FragPresenterTag(
): PresenterDelegateProvider<ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View> = tag()


typealias V4DialogFragPresenterTag<ARG, RET> = PresenterTag<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog>

inline fun <reified ARG : Parcelable, reified RET : Parcelable> v4DialogFragPresenterTag(
): PresenterDelegateProvider<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog> = tag()


@Suppress("UNCHECKED_CAST") // it's safe here, because tag doesn't store anything type-dependent
inline fun <reified ARG : Parcelable, reified RET : Parcelable, reified HOST, reified PARENT, reified VIEW> tag(
) =
        PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW>(
                ARG::class.java.hashCode(),
                RET::class.java.hashCode(),
                HOST::class.java.hashCode(),
                PARENT::class.java.hashCode(),
                VIEW::class.java.hashCode()
        )

class PresenterDelegateProvider<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW>
@PublishedApi internal constructor(
        private val argClassHash: Int,
        private val retClassHash: Int,
        private val hostClassHash: Int,
        private val parentClassHash: Int,
        private val viewClassHash: Int
) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) =
            PresenterTag<ARG, RET, HOST, PARENT, VIEW>(
                    prop.name, argClassHash, retClassHash, hostClassHash, parentClassHash, viewClassHash
            )
}
