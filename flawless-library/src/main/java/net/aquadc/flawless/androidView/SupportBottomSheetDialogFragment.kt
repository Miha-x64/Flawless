package net.aquadc.flawless.androidView

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.util.FragmentExchange
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.SupportBottomSheetDialogFragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupportBottomSheetDialogFragPresenterTag

class SupportBottomSheetDialogFragment<in ARG : Parcelable, RET : Parcelable>
    : BottomSheetDialogFragment, Host<RET> {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: SupportBottomSheetDialogFragPresenterTag<ARG, RET, *>, arg: ARG) {
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


    // Host copy-paste impl


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
    override val exchange: Host.Exchange<RET>
        get() = _exchange ?: FragmentExchange<RET>(this).also { _exchange = it }


    // Dialog-specific

    private val arg: ARG
        get() = arguments.getParcelable("arg")

    var onCancel: (() -> Unit)? = null

    override fun onCancel(dialog: DialogInterface?) {
        onCancel?.invoke()
    }


    // own code


    private var presenter: SupportBottomSheetDialogFragPresenter<ARG, RET, Parcelable>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (presenter == null) {
            val presenter =
                    findPresenterFactory().createPresenter(arguments.getParcelable("tag"))
            this.presenter = presenter as SupportBottomSheetDialogFragPresenter<ARG, RET, Parcelable> // erase state type
            presenter.onAttach(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        _exchange = savedInstanceState?.getParcelable<FragmentExchange<RET>>("res cbs")?.also { it.fragment = this }
        presenter!!.onCreate(this, arg, savedInstanceState?.getParcelable("presenter"))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            presenter!!.createView(this, context, arg, savedInstanceState?.getParcelable("presenter"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter!!.onViewCreated(this, view, arg, savedInstanceState?.getParcelable("presenter"))
        visibilityState = VisibilityState.Visible
    }

    // dirty hack from https://stackoverflow.com/a/12434038
    override fun onDestroyView() {
        visibilityState = VisibilityState.Uninitialized
        presenter!!.onViewDestroyed(this)
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter!!.onDestroy(this)
        presenter = null
        _exchange?.fragment = null
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (_exchange?.deliverResult(presenter!!, requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        _exchange?.deliverPermissionResult(presenter!!, requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("res cbs", _exchange)
        outState.putParcelable("presenter", presenter!!.saveState())
    }

    override fun toString(): String = toString(super.toString(), presenter)

}

typealias ConsumerSupportBottomSheetDialogFragment<ARG> = SupportBottomSheetDialogFragment<ARG, ParcelUnit>
typealias SupplierSupportBottomSheetDialogFragment<RET> = SupportBottomSheetDialogFragment<ParcelUnit, RET>
typealias ActionSupportBottomSheetDialogFragment = SupportBottomSheetDialogFragment<ParcelUnit, ParcelUnit>
