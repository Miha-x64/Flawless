@file:Suppress("NOTHING_TO_INLINE") // I ❤️ inline!
package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.Presenter
import kotlin.reflect.KProperty


@Suppress(
        "UNUSED_PARAMETER", // required for inference hack
        "UNCHECKED_CAST" // a bit quirky, should be safe
)
inline fun <
        reified ARG : Parcelable,
        reified RET : Parcelable,
        reified HOST : Host<RET>,
        reified PARENT,
        reified VIEW,
        reified PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW, *>
        > tag(
        dummy: Dummy<PRESENTER>
): PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW, PRESENTER> = PresenterDelegateProviderImpl.apply {
    beforeProvideDelegate(
            PRESENTER::class.java.name,
            ARG::class.java.name,
            RET::class.java.name
    )
} as PresenterDelegateProvider<ARG, RET, HOST, PARENT, VIEW, PRESENTER>


@Suppress("UNUSED")
interface Dummy<out T>
object DummyImpl : Dummy<Nothing>


inline fun <T> of(): Dummy<T> = DummyImpl

interface PresenterDelegateProvider<in ARG : Parcelable, RET : Parcelable, HOST : Host<RET>, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW, *>> {
    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): PresenterTag<ARG, RET, HOST, PARENT, VIEW, PRESENTER>
}

@PublishedApi
internal object PresenterDelegateProviderImpl :
        PresenterDelegateProvider<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {

    private var presenterClassName: String? = null
    private var argClassName: String? = null
    private var retClassName: String? = null

    @PublishedApi
    internal fun beforeProvideDelegate(
            presenterClassName: String, argClassName: String, retClassName: String
    ) {
        check(this.presenterClassName == null)
        check(this.argClassName == null)
        check(this.retClassName == null)

        this.presenterClassName = presenterClassName
        this.argClassName = argClassName
        this.retClassName = retClassName
    }

    override fun provideDelegate(thisRef: Any?, prop: KProperty<*>): PresenterTag<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        val presenterClassName = this.presenterClassName!!
        val argClassName = this.argClassName!!
        val retClassName = this.retClassName!!

        this.presenterClassName = null
        this.argClassName = null
        this.retClassName = null

        return PresenterTag(
                thisRef?.javaClass?.name ?: "local", prop.name,
                presenterClassName, argClassName, retClassName
        )
    }

}
