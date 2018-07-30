package net.aquadc.flawlesssampleapp

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import net.aquadc.flawless.screen.VisibilityState
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.screen.StatelessConsumerSupportFragScreen
import net.aquadc.flawless.screen.ViewFirstShownListener
import net.aquadc.flawless.screen.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.screen.StatelessScreenArgs
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor


class PagerItemScreen(
        private val args: StatelessScreenArgs<ParcelInt, SupportFragment>
) : StatelessConsumerSupportFragScreen<ParcelInt>, VisibilityStateListener {

    init {
        val (arg, host, _) = args
        host.addVisibilityStateListener(ViewFirstShownListener {
            Toast.makeText(host.activity, "Fragment #${arg.value} should load data now.", Toast.LENGTH_SHORT).show()
        })
    }

    override fun createView(parent: ViewGroup): View =
            TextView(args.host.activity).apply {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                text = "Uninitialized"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
                textColor = Color.BLACK
                gravity = Gravity.CENTER
            }

    private var view: TextView? = null

    override fun viewAttached(view: View) {
        this.view = view as TextView
        args.host.addVisibilityStateListener(this)
    }

    override fun onVisibilityStateChanged(host: Host, old: VisibilityState, new: VisibilityState) {
        view!!.let {
            it.text = new.name
            it.backgroundColor = new.colour
        }
    }

    override fun disposeView() {
        args.host.removeVisibilityStateListener(this)
        this.view = null
    }

    override fun destroy() {
    }

    private val VisibilityState.colour get() = when (this) {
        VisibilityState.Uninitialized -> 0xFF000000.toInt()
        VisibilityState.Invisible -> 0xFFAAAAAA.toInt()
        VisibilityState.Visible -> 0xFFFAFAFA.toInt()
    }

}
