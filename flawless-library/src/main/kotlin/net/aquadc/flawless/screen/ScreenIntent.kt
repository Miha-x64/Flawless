@file:Suppress("NOTHING_TO_INLINE")
package net.aquadc.flawless.screen

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.ScreenTag

/**
 * Holds arguments for a [Screen].
 * Typically gets passed into Screen's constructor.
 */
class ScreenArgs<out ARG : Parcelable, out HOST : Host, out STATE : Parcelable>(
        val arg: ARG,
        val host: HOST,
        val state: STATE?
) {

    inline operator fun component1(): ARG = arg
    inline operator fun component2(): HOST = host
    inline operator fun component3(): STATE? = state

}

typealias ActionScreenArgs<HOST, STATE> = ScreenArgs<ParcelUnit, HOST, STATE>
typealias StatelessScreenArgs<ARG, HOST> = ScreenArgs<ARG, HOST, ParcelUnit>
typealias StatelessActionScreenArgs<HOST> = ScreenArgs<ParcelUnit, HOST, ParcelUnit>


/**
 * Describes a request for a [Screen]. Consumed by [ScreenFactory], [select], [then].
 */
class ScreenIntent<ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable>(
        val tag: ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE>,
        val args: ScreenArgs<ARG, HOST, STATE>
) {

    /**
     * Acts like a specific `when` case — creates a screen if tag matches.
     */
    inline infix fun <ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, STATE : Parcelable, SCR : Screen<ARG, RET, HOST, PARENT, VIEW, STATE>>
            ScreenTag<ARG, RET, HOST, PARENT, VIEW, STATE>.then(create: ScreenIntent<ARG, RET, HOST, PARENT, VIEW, STATE>.() -> SCR) {
        if (Select.matchingScreen != null) return

        val request = Select.currentIntent
                ?: error { "called from wrong context" }

        // this@then === request.tag when arguments bundle was saved by reference
        // this@then == request.tag when arguments were parceled
        if (this@then == request.tag) {
            @Suppress("UNCHECKED_CAST")
            this@ScreenIntent as ScreenIntent<ARG, RET, HOST, PARENT, VIEW, STATE>

            Select.matchingScreen = create()
        }
    }

}

typealias AnyScreenIntent = ScreenIntent<*, *, *, *, *, *>
