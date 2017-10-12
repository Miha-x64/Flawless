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


typealias V4FragPresenterTag<ARG, RET> = PresenterTag<ARG, RET, MvpFragmentV4<ARG>, ViewGroup?, View>

typealias V4DialogFragPresenterTag<ARG, RET> = PresenterTag<ARG, RET, MvpDialogFragmentV4<ARG>, Context, Dialog>


inline fun <reified ARG : Parcelable, reified RET : Parcelable, reified HOST, reified PARENT, reified VIEW> tag(
        dummy: Dummy<Presenter<ARG, RET, HOST, PARENT, VIEW>>
) =
        PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW>(
                ARG::class.java.hashCode(),
                RET::class.java.hashCode(),
                HOST::class.java.hashCode(),
                PARENT::class.java.hashCode(),
                VIEW::class.java.hashCode()
        )

interface Dummy<out T>
object DummyImpl : Dummy<Any>
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <T> of() = DummyImpl as Dummy<T>

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
