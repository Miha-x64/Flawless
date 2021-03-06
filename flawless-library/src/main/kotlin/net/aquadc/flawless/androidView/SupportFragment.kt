package net.aquadc.flawless.androidView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.spySavedState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.SupportFragmentHost
import net.aquadc.flawless.screen.VisibilityState
import net.aquadc.flawless.androidView.util.DeliverResultIfTargetAlive
import net.aquadc.flawless.androidView.util.FragmentExchange
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.screen.AnyScreen
import net.aquadc.flawless.screen.ScreenFactory
import net.aquadc.flawless.screen.SupportFragScreen
import net.aquadc.flawless.screen.VisibilityStateListener
import net.aquadc.flawless.screen.AnyScreenIntent
import net.aquadc.flawless.tag.SupportFragScreenTag

/**
 * A host for screens.
 * Implements [ContextHost] and thus supports [startActivity] etc.
 * Proxies [createScreen] to encapsulated [Screen] if it is [ScreenFactory].
 */
@SuppressLint("ValidFragment") // shut up, I know better
class SupportFragment : Fragment, ContextHost, SupportFragmentHost, ScreenFactory {

    @Deprecated(message = "used by framework", level = DeprecationLevel.HIDDEN) @Suppress("UNUSED")
    constructor()

    @PublishedApi
    internal constructor(tag: SupportFragScreenTag<*, *, *>, arg: Parcelable) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    companion object {
        @Suppress("NOTHING_TO_INLINE")
        inline operator fun <ARG : Parcelable, RET : Parcelable, STATE : Parcelable> invoke(
                tag: SupportFragScreenTag<ARG, RET, STATE>, arg: ARG
        ) = SupportFragment(tag, arg)
    }

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    // Host impl

    override val fragment: Fragment get() = this

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


    private var _exchange: FragmentExchange<Parcelable>? = null
    override val exchange: ContextHost.Exchange
        get() = _exchange ?: FragmentExchange<Parcelable>(this, screen).also { _exchange = it }


    // own code


    private var screen: SupportFragScreen<Parcelable, Parcelable, Parcelable>? = null
    private var isStateSaved = false

    /**
     * Shouldn't ever exist, but may be necessary for interop.
     */
    @Deprecated("After refactoring, you may never need it.")
    fun getScreen(): SupportFragScreen<*, *, *>? = screen as SupportFragScreen<*, *, *>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (screen == null) { // may be re-attached to new Activity
            val savedInstanceState = spySavedState
            _exchange = savedInstanceState?.getParcelable("res cbs")
            // screen may request 'exchange' im its 'init' block
            screen = createScreen(savedInstanceState) as SupportFragScreen<Parcelable, Parcelable, Parcelable>
            // he-he, cyclic dependency
            _exchange?.attachTo(this, screen!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            screen!!.createView(container!!)
            //   okay Google, this sucks ^^

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        screen!!.viewAttached(view!!)
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
        screen!!.disposeView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        val screen = screen!!
        if (isFinishing(isStateSaved) && targetFragment != null) {
            exchange
            handler.post(
                    DeliverResultIfTargetAlive(_exchange!!, targetFragment, screen.returnValue, isStateSaved /*(!)*/)
            //      ^ contains `exchange.attachTo(null, null)` line itself
            )
        } else {
            _exchange?.attachTo(null, null)
        }

        screen.destroy()
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

    override fun createScreen(intent: AnyScreenIntent): AnyScreen {
        val screen = screen

        if (screen == null)
            throw IllegalStateException("Screen is not attached.")

        if (screen !is ScreenFactory)
            throw UnsupportedOperationException("Screen $screen does not implement ScreenFactory.")

        return screen.createScreen(intent)
    }

    override fun toString(): String = toString(super.toString(), screen)

}
