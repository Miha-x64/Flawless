@file:Suppress("NOTHING_TO_INLINE") // I ❤️ inline!
package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.screen.Screen
import kotlin.reflect.KProperty


@Suppress(
        "UNUSED_PARAMETER", // required for inference hack
        "UNCHECKED_CAST" // a bit quirky, should be safe
)
inline fun <
        reified ARG : Parcelable,
        reified RET : Parcelable,
        HOST : Host,
        PARENT,
        VIEW,
        STATE : Parcelable,
        reified SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>
        > tag(
        dummy: Dummy<SCR>
): ScreenDelegateProvider<ARG, RET, HOST, PARENT, VIEW, STATE, SCR> = ScreenDelegateProviderImpl.apply {
    beforeProvideDelegate(
            SCR::class.java.name,
            ARG::class.java.name,
            RET::class.java.name
    )
} as ScreenDelegateProvider<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>


@Suppress("UNUSED")
interface Dummy<out T>
object DummyImpl : Dummy<Nothing>


inline fun <T> of(): Dummy<T> = DummyImpl

interface ScreenDelegateProvider<in ARG : Parcelable, out RET : Parcelable, in HOST : Host, PARENT, VIEW, STATE : Parcelable,
        SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>> {
    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>?): ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>
}

@PublishedApi
internal object ScreenDelegateProviderImpl :
        ScreenDelegateProvider<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {

    private var screenClassName: String? = null
    private var argClassName: String? = null
    private var retClassName: String? = null

    @PublishedApi
    internal fun beforeProvideDelegate(
            screenClassName: String, argClassName: String, retClassName: String
    ) {
        check(this.screenClassName == null)
        check(this.argClassName == null)
        check(this.retClassName == null)

        this.screenClassName = screenClassName
        this.argClassName = argClassName
        this.retClassName = retClassName
    }

    override fun provideDelegate(thisRef: Any?, prop: KProperty<*>?): ScreenTag<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        val screenClassName = this.screenClassName!!
        val argClassName = this.argClassName!!
        val retClassName = this.retClassName!!

        this.screenClassName = null
        this.argClassName = null
        this.retClassName = null

        return ScreenTag(
                thisRef?.javaClass?.name ?: "local", prop?.name ?: "none",
                screenClassName, argClassName, retClassName
        )
    }

}
