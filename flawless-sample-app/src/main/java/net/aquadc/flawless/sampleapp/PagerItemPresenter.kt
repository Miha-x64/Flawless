package net.aquadc.flawless.sampleapp

import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.ConsumerMvpV4Fragment
import net.aquadc.flawless.extension.addViewFirstShownListener
import net.aquadc.flawless.implementMe.StatelessConsumerV4FragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor

class PagerItemPresenter : StatelessConsumerV4FragPresenter<ParcelInt>, VisibilityStateListener {

    override fun onCreate(host: ConsumerMvpV4Fragment<ParcelInt>, arg: ParcelInt, state: ParcelUnit?) {
        host.addViewFirstShownListener {
            Toast.makeText(it.activity, "Fragment #${arg.value} should load data now.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun createView(host: ConsumerMvpV4Fragment<ParcelInt>, parent: ViewGroup?, arg: ParcelInt, state: ParcelUnit?): View =
            TextView(host.activity).apply {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                text = "Uninitialized"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
                textColor = Color.BLACK
                gravity = Gravity.CENTER
            }

    private var view: TextView? = null

    override fun onViewCreated(host: ConsumerMvpV4Fragment<ParcelInt>, view: View, arg: ParcelInt, state: ParcelUnit?) {
        this.view = view as TextView
        host.addVisibilityStateListener(this)
    }

    override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) {
        view!!.let {
            it.text = new.name
            it.backgroundColor = new.colour
        }
    }

    override fun onViewDestroyed(host: ConsumerMvpV4Fragment<ParcelInt>) {
        host.removeVisibilityStateListener(this)
        this.view = null
    }

    override fun onDestroy(host: ConsumerMvpV4Fragment<ParcelInt>) {
    }

    private val VisibilityState.colour get() = when (this) {
        VisibilityState.Uninitialized -> 0xFF000000.toInt()
        VisibilityState.Invisible -> 0xFFAAAAAA.toInt()
        VisibilityState.Visible -> 0xFFFAFAFA.toInt()
    }

}
