package net.aquadc.flawless.sampleapp

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.StatelessConsumerSupportFragScreen
import net.aquadc.flawless.implementMe.ViewFirstShownListener
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor


class PagerItemScreen : StatelessConsumerSupportFragScreen<ParcelInt>, VisibilityStateListener {

    override fun onCreate(host: SupportFragment, arg: ParcelInt, state: ParcelUnit?) {
        host.addVisibilityStateListener(ViewFirstShownListener {
            Toast.makeText(host.activity, "Fragment #${arg.value} should load data now.", Toast.LENGTH_SHORT).show()
        })
    }

    override fun createView(host: SupportFragment, parent: ViewGroup, arg: ParcelInt, state: ParcelUnit?): View =
            TextView(host.activity).apply {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                text = "Uninitialized"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
                textColor = Color.BLACK
                gravity = Gravity.CENTER
            }

    private var view: TextView? = null

    override fun onViewCreated(host: SupportFragment, view: View, arg: ParcelInt, state: ParcelUnit?) {
        this.view = view as TextView
        host.addVisibilityStateListener(this)
    }

    override fun onVisibilityStateChanged(host: Host, old: VisibilityState, new: VisibilityState) {
        view!!.let {
            it.text = new.name
            it.backgroundColor = new.colour
        }
    }

    override fun onViewDestroyed(host: SupportFragment) {
        host.removeVisibilityStateListener(this)
        this.view = null
    }

    override fun onDestroy(host: SupportFragment) {
    }

    private val VisibilityState.colour get() = when (this) {
        VisibilityState.Uninitialized -> 0xFF000000.toInt()
        VisibilityState.Invisible -> 0xFFAAAAAA.toInt()
        VisibilityState.Visible -> 0xFFFAFAFA.toInt()
    }

}
