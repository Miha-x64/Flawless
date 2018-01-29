package net.aquadc.flawless.androidView.util

import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.androidView.isFinishing


internal class DeliverResultIfTargetAlive<RET : Parcelable>(
        private val exchange: FragmentExchange<RET>,
        private val target: Fragment,
        private val value: RET?,
        private val isStateSaved: Boolean
) : Runnable {

    override fun run() {
        if (!target.isFinishing(isStateSaved)) {
            exchange.deliver(value)
        }
        exchange.fragment = null
    }

}
