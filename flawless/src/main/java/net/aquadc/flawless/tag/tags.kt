package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpDialogFragmentV4
import net.aquadc.flawless.androidView.MvpFragmentV4
import net.aquadc.flawless.implementMe.Presenter
import kotlin.reflect.KProperty


typealias V4FragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View, PRESENTER>

typealias V4DialogFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog, PRESENTER>


inline fun <
        reified ARG : Parcelable,
        reified RET : Parcelable,
        reified HOST,
        reified PARENT,
        reified VIEW,
        reified PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW>
        > tag(
        dummy: Dummy<PRESENTER>
) =
        PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW, PRESENTER>(
                PRESENTER::class.java.name,
                ARG::class.java.name,
                RET::class.java.name,
                HOST::class.java.name,
                PARENT::class.java.name,
                VIEW::class.java.name
        )

interface Dummy<out T>
object DummyImpl : Dummy<Any>
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <T> of() = DummyImpl as Dummy<T>

class PresenterDelegateProvider<
        ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW>>
@PublishedApi internal constructor(
        private val presenterClassName: String,
        private val argClassHashName: String,
        private val retClassName: String,
        private val hostClassName: String,
        private val parentClassName: String,
        private val viewClassName: String
) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) =
            PresenterTag<ARG, RET, HOST, PARENT, VIEW, PRESENTER>(
                    prop.name,
                    presenterClassName, argClassHashName, retClassName, hostClassName, parentClassName, viewClassName
            )
}
