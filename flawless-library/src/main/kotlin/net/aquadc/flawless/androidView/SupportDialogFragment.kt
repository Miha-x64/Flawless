package net.aquadc.flawless.androidView

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import android.view.View
import net.aquadc.flawless.SupportFragmentHost
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.util.DeliverResultIfTargetAlive
import net.aquadc.flawless.androidView.util.FragmentExchange
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.Screen
import net.aquadc.flawless.implementMe.SupportDialogFragScreen
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.tag.SupportDialogFragScreenTag

/**
 * [AppCompatDialogFragment] host for [Screen].
 * @see SupportFragment for details.
 */
@SuppressLint("ValidFragment")
class SupportDialogFragment : AppCompatDialogFragment, ContextHost, SupportFragmentHost {

    @Deprecated(message = "used by framework", level = DeprecationLevel.HIDDEN) @Suppress("UNUSED")
    constructor()

    @PublishedApi
    internal constructor(tag: SupportDialogFragScreenTag<*, *, *>, arg: Parcelable) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    companion object {
        @Suppress("NOTHING_TO_INLINE")
        inline operator fun <ARG : Parcelable, RET : Parcelable> invoke(
                tag: SupportDialogFragScreenTag<ARG, RET, *>, arg: ARG
        ) = SupportDialogFragment(tag, arg)
    }

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    // Host copy-paste impl

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
        get() = _exchange ?: FragmentExchange<Parcelable>(this, screen!!).also { _exchange = it }


    // Dialog-specific code

    private val arg: Parcelable
        get() = arguments.getParcelable("arg")

    var onCancel: (() -> Unit)? = null

    override fun onCancel(dialog: DialogInterface?) {
        onCancel?.invoke()
    }


    // own code

    private var screen: SupportDialogFragScreen<Parcelable, Parcelable, Parcelable>? = null
    private var isStateSaved = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (screen == null) {
            val screen =
                    findScreenFactory().createScreen(arguments.getParcelable("tag"), this)
            this.screen = screen as SupportDialogFragScreen<Parcelable, Parcelable, Parcelable>
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        _exchange = savedInstanceState?.getParcelable<FragmentExchange<Parcelable>>("res cbs")?.also { it.attachTo(this, screen!!) }
        screen!!.onCreate(this, arg, savedInstanceState?.getParcelable("screen"))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            screen!!.createView(this, context, arg, savedInstanceState?.getParcelable("screen"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        screen!!.onViewCreated(this, dialog, arg, savedInstanceState?.getParcelable("screen"))
        visibilityState = VisibilityState.Visible
    }

    override fun onStart() {
        super.onStart()
        isStateSaved = false
    }

    override fun onResume() {
        super.onResume()
        isStateSaved = false
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
        dialog?.setDismissMessage(null) // dirty hack from https://stackoverflow.com/a/12434038
        super.onDestroyView()
    }

    override fun onDestroy() {
        val screen = screen!!
        if (isFinishing(isStateSaved) && targetFragment != null) {
            exchange
            handler.post(
                    DeliverResultIfTargetAlive(_exchange!!, targetFragment, screen.returnValue, isStateSaved)
            )
        } else {
            _exchange?.attachTo(null, null)
        }

        screen.onDestroy(this)
        this.screen = null
        super.onDestroy()
        isStateSaved = false
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

    override fun toString(): String = toString(super.toString(), screen)

}

@Deprecated("useless now", ReplaceWith("SupportDialogFragment"), DeprecationLevel.ERROR)
typealias ConsumerSupportDialogFragment<ARG> = SupportDialogFragment

@Deprecated("useless now", ReplaceWith("SupportDialogFragment"), DeprecationLevel.ERROR)
typealias SupplierSupportDialogFragment<RET> = SupportDialogFragment

@Deprecated("useless now", ReplaceWith("SupportDialogFragment"), DeprecationLevel.ERROR)
typealias ActionSupportDialogFragment = SupportDialogFragment
