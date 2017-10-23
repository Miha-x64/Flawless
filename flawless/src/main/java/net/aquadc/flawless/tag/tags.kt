@file:Suppress("NOTHING_TO_INLINE") // I ❤️ inline!
package net.aquadc.flawless.tag

import android.app.Dialog
import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.Presenter
import kotlin.reflect.KProperty


typealias V4FragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpV4Fragment<ARG>, ViewGroup?, View, PRESENTER>

typealias V4DialogFragPresenterTag<ARG, RET, PRESENTER> = PresenterTag<
        ARG, RET, MvpV4DialogFragment<ARG>, Context, Dialog, PRESENTER>


@Suppress(
        "UNUSED_PARAMETER", // required for inference hack
        "UNCHECKED_CAST" // a bit quirky, should be safe
)
inline fun <
        reified ARG : Parcelable,
        reified RET : Parcelable,
        reified HOST,
        reified PARENT,
        reified VIEW,
        reified PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW>
        > tag(
        dummy: Dummy<PRESENTER>
): PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW, PRESENTER> = PresenterDelegateProviderImpl.apply {
    beforeProvideDelegate(
            PRESENTER::class.java.name,
            ARG::class.java.name,
            RET::class.java.name,
            HOST::class.java.name,
            PARENT::class.java.name,
            VIEW::class.java.name
    )
} as PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW, PRESENTER>


@Suppress("UNUSED")
interface Dummy<out T>
object DummyImpl : Dummy<Nothing>


inline fun <T> of(): Dummy<T> = DummyImpl

interface PresenterDelegateProvider<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW>> {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): PresenterTag<ARG, RET, HOST, PARENT, VIEW, PRESENTER>
}

@PublishedApi
internal object PresenterDelegateProviderImpl :
        PresenterDelegateProvider<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {

    private var presenterClassName: String? = null
    private var argClassName: String? = null
    private var retClassName: String? = null
    private var hostClassName: String? = null
    private var parentClassName: String? = null
    private var viewClassName: String? = null

    @PublishedApi
    internal fun beforeProvideDelegate(
            presenterClassName: String, argClassName: String, retClassName: String, hostClassName: String,
            parentClassName: String, viewClassName: String
    ) {
        check(this.presenterClassName == null)
        check(this.argClassName == null)
        check(this.retClassName == null)
        check(this.hostClassName == null)
        check(this.parentClassName == null)
        check(this.viewClassName == null)

        this.presenterClassName = presenterClassName
        this.argClassName = argClassName
        this.retClassName = retClassName
        this.hostClassName = hostClassName
        this.parentClassName = parentClassName
        this.viewClassName = viewClassName
    }

    override fun provideDelegate(thisRef: Any, prop: KProperty<*>): PresenterTag<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        val presenterClassName = this.presenterClassName!!
        val argClassName = this.argClassName!!
        val retClassName = this.retClassName!!
        val hostClassName = this.hostClassName!!
        val parentClassName = this.parentClassName!!
        val viewClassName = this.viewClassName!!

        this.presenterClassName = null
        this.argClassName = null
        this.retClassName = null
        this.hostClassName = null
        this.parentClassName = null
        this.viewClassName = null

        return PresenterTag(
                prop.name,
                presenterClassName, argClassName, retClassName, hostClassName, parentClassName, viewClassName
        )
    }

}
