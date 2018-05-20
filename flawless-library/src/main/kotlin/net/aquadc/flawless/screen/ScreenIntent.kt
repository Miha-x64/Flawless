@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.screen

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.ScreenTag

/**
 * Holds arguments for a [Screen].
 */
class ScreenArgs<ARG : Parcelable, HOST : Host, STATE : Parcelable>(
        val arg: ARG,
        val host: HOST,
        val state: STATE?
) {

    inline operator fun component1() = arg
    inline operator fun component2() = host
    inline operator fun component3() = state

}

fun <HOST : Host, STATE : Parcelable> ScreenArgs(host: HOST, state: STATE?) =
        ScreenArgs(ParcelUnit, host, state)

typealias ActionScreenArgs<HOST, STATE> = ScreenArgs<ParcelUnit, HOST, STATE>
typealias StatelessScreenArgs<ARG, HOST> = ScreenArgs<ARG, HOST, ParcelUnit>
typealias StatelessActionScreenArgs<HOST> = ScreenArgs<ParcelUnit, HOST, ParcelUnit>

/**
 * Describes a request for a [Screen]. Used in [ScreenFactory], [select], [then].
 */
class ScreenIntent<ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable,
        SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>>(
        val tag: ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>,
        val args: ScreenArgs<ARG, HOST, STATE>
) {

    /**
     * Acts like a specific `when` case — creates a screen if tag matches.
     */
    inline infix fun <ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable, SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>>
            ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>.then(create: ScreenIntent<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>.() -> SCR) {
        if (Select.matchingScreen != null) return

        val request = Select.currentIntent
                ?: error { "called from wrong context" }

        if (this@then === request.tag) {
            @Suppress("UNCHECKED_CAST")
            this@ScreenIntent as ScreenIntent<ARG, RET, HOST, PARENT, VIEW, STATE, SCR>

            Select.matchingScreen = create()
        }
    }

}

typealias AnyScreenIntent = ScreenIntent<*, *, *, *, *, *, *>
