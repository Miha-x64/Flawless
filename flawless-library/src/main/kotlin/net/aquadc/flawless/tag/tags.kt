@file:Suppress("NOTHING_TO_INLINE") // I ❤️ inline!
package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.screen.Screen
import kotlin.reflect.KProperty


/**
 * Creates a [ScreenTag] for type parameters of the given [SCR].
 */
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
        dummy: List<SCR>
): ScreenDelegateProvider<ARG, RET, HOST, PARENT, VIEW, STATE, SCR> = screenDelegateProvider.apply {
    beforeProvideDelegate(
            SCR::class.java.name,
            ARG::class.java.name,
            RET::class.java.name
    )
} as ScreenDelegateProvider<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>

@Suppress("UNCHECKED_CAST")
fun <ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable, SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>
        > tag(
        name: String, argClass: Class<ARG>, retClass: Class<RET>, screenClass: Class<SCR>
): ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE> =
        ScreenTag(
                "unknown", name,
                screenClass.name, argClass.name, retClass.name
        )

/**
 * Just an inference hack for mapping type parameters
 * @return value is ignored
 */
inline fun <T> of(): List<T> = emptyList()

@[JvmField JvmSynthetic PublishedApi] internal val screenDelegateProvider =
        ScreenDelegateProvider<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing>()

class ScreenDelegateProvider<
        ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable,
        SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>> internal constructor() {

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

    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE> {
        val screenClassName = this.screenClassName!!
        val argClassName = this.argClassName!!
        val retClassName = this.retClassName!!

        this.screenClassName = null
        this.argClassName = null
        this.retClassName = null

        return ScreenTag(
                thisRef.javaClass.name ?: "local", prop.name,
                screenClassName, argClassName, retClassName
        )
    }

}
