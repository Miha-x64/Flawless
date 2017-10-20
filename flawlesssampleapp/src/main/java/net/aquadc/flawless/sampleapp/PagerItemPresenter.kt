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
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.extension.addViewFirstShownListener
import net.aquadc.flawless.implementMe.ConsumerV4FragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelInt
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor

class PagerItemPresenter : ConsumerV4FragPresenter<ParcelInt>, VisibilityStateListener {

    override fun onAttach(host: MvpV4Fragment<ParcelInt>, arg: ParcelInt) {
        host.addViewFirstShownListener {
            Toast.makeText(it.activity, "Fragment #${arg.value} should load data now.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun createView(host: MvpV4Fragment<ParcelInt>, parent: ViewGroup?, arg: ParcelInt): View =
            TextView(host.activity).apply {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                text = "Uninitialized"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
                textColor = Color.BLACK
                gravity = Gravity.CENTER
            }

    private var view: TextView? = null

    override fun onViewCreated(host: MvpV4Fragment<ParcelInt>, view: View, arg: ParcelInt) {
        this.view = view as TextView
        host.addVisibilityStateListener(this)
    }

    override fun onVisibilityStateChanged(host: Fragment, old: VisibilityState, new: VisibilityState) {
        view!!.let {
            it.text = new.name
            it.backgroundColor = new.colour
        }
    }

    override fun onViewDestroyed(host: MvpV4Fragment<ParcelInt>) {
        host.removeVisibilityStateListener(this)
        this.view = null
    }

    override fun onDetach() {
    }

    private val VisibilityState.colour get() = when (this) {
        VisibilityState.Uninitialized -> 0xFF000000.toInt()
        VisibilityState.Invisible -> 0xFFAAAAAA.toInt()
        VisibilityState.Visible -> 0xFFFAFAFA.toInt()
    }

}
