package net.aquadc.flawless.androidView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.util.DeliverResultIfTargetAlive
import net.aquadc.flawless.androidView.util.FragmentExchange
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.ScreenFactory
import net.aquadc.flawless.implementMe.SupportFragScreen
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.AnyScreenTag
import net.aquadc.flawless.tag.SupportFragScreenTag


class SupportFragment<in ARG : Parcelable, out RET : Parcelable>
    : Fragment, Host, ScreenFactory {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    @SuppressLint("ValidFragment")
    constructor(tag: SupportFragScreenTag<ARG, RET, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    // Host impl


    private var visibilityListeners: VisibilityStateListeners? = null
    override var visibilityState = VisibilityState.Uninitialized
        private set(new) {
            val old = field
            field = new
            visibilityListeners?.updated(this, old, new)
        }
    override fun addVisibilityStateListener(listener: VisibilityStateListener) =
            (visibilityListeners ?: VisibilityStateListeners().also { visibilityListeners = it }).add(listener)
    override fun removeVisibilityStateListener(listener: VisibilityStateListener) {
        visibilityListeners?.remove(listener)
    }


    private var _exchange: FragmentExchange<RET>? = null
    override val exchange: Host.Exchange
        get() = _exchange ?: FragmentExchange<RET>(this).also { _exchange = it }


    // own code


    private var screen: SupportFragScreen<ARG, RET, Parcelable>? = null
    private var isStateSaved = false

    /**
     * Shouldn't ever exist, but may be necessary for interop.
     */
    @Deprecated("After refactoring, you may never need it.")
    fun getScreen(): SupportFragScreen<*, *, *>? = screen as SupportFragScreen<*, *, *>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (screen == null) { // may be re-attached to new Activity
            val screen =
                    findScreenFactory().createScreen(arguments.getParcelable("tag"))
            this.screen = screen as SupportFragScreen<ARG, RET, Parcelable> // erase state type
            screen.onAttach(this)
        }
    }

    private val arg: ARG get() = arguments.getParcelable("arg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        _exchange = savedInstanceState?.getParcelable<FragmentExchange<RET>>("res cbs")?.also { it.fragment = this }
        screen!!.onCreate(this, arg, savedInstanceState?.getParcelable("screen"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            screen!!.createView(this, container, arg, savedInstanceState?.getParcelable("screen"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        screen!!.onViewCreated(this, view!!, arg, savedInstanceState?.getParcelable("screen"))
        // you can set visibilityStateListener in onViewCreated, and will receive an update then
        visibilityState = if (userVisibleHint) VisibilityState.Visible else VisibilityState.Invisible
    }

    override fun onStart() {
        super.onStart()
        isStateSaved = false
    }

    override fun onResume() {
        super.onResume()
        isStateSaved = false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded && view != null && visibilityState != VisibilityState.Uninitialized) {
            visibilityState = if (isVisibleToUser) VisibilityState.Visible else VisibilityState.Invisible
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("res cbs", _exchange)
        outState.putParcelable("screen", screen!!.saveState())
        isStateSaved = true
    }

    override fun onDestroyView() {
        visibilityState = VisibilityState.Uninitialized
        screen!!.onViewDestroyed(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        val screen = screen!!
        if (isFinishing(isStateSaved) && targetFragment != null) {
            exchange
            activity.window.decorView.handler.post(
                    DeliverResultIfTargetAlive(_exchange!!, targetFragment, screen.returnValue, isStateSaved /*(!)*/)
            //      ^ contains `exchange.fragment = null` string itself
            )
        } else {
            _exchange?.fragment = null
        }

        screen.onDestroy(this)
        this.screen = null
        super.onDestroy()
        isStateSaved = false

        /*
         ! We're using our own [isStateSaved] flag to check whether [targetFragment] is finishing/removing.
           We can't be sure [targetFragment] has its own [isStateSaved] because this may be a normal fragment.
           Hope there's no such situation when one fragment saves its state, while another does not.
         */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        isStateSaved = false
        if (_exchange?.deliverResult(screen!!, requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        _exchange?.deliverPermissionResult(screen!!, requestCode, permissions, grantResults)
    }

    override fun createScreen(tag: AnyScreenTag): AnyScreen {
        val screen = screen

        if (screen == null)
            throw IllegalStateException("Screen is not attached.")

        if (screen !is ScreenFactory)
            throw UnsupportedOperationException("Screen $screen does not implement ScreenFactory.")

        return screen.createScreen(tag)
    }

    override fun toString(): String = toString(super.toString(), screen)

}

typealias ConsumerSupportFragment<ARG> = SupportFragment<ARG, ParcelUnit>
typealias SupplierSupportFragment<RET> = SupportFragment<ParcelUnit, RET>
typealias ActionSupportFragment = SupportFragment<ParcelUnit, ParcelUnit>
