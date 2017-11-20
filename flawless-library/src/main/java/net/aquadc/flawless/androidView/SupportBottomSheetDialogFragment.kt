package net.aquadc.flawless.androidView

import android.app.Activity
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
import net.aquadc.flawless.androidView.util.ResultCallbacks
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.AnyPresenter
import net.aquadc.flawless.implementMe.SupportBottomSheetDialogFragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupportBottomSheetDialogFragPresenterTag

class SupportBottomSheetDialogFragment<in ARG : Parcelable, RET : Parcelable>
    : BottomSheetDialogFragment, Host<RET>, Host.Exchange<RET> {

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


    // Host.Exchange copy-paste impl


    override val exchange: Host.Exchange<RET> get() = this

    private var _resultCallbacks: ResultCallbacks? = null

    private val resultCallbacks: ResultCallbacks
        get() = _resultCallbacks ?: ResultCallbacks().also { _resultCallbacks = it }

    override fun <PRESENTER : AnyPresenter, RET> registerResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
            cancellationCallback: ParcelFunction1<PRESENTER, Unit>
    ) = resultCallbacks.addOrThrow(this, requestCode, resultCallback, cancellationCallback)

    override fun <PRESENTER : AnyPresenter> registerRawResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction3<PRESENTER, Int, Intent?, Unit>
    ) = resultCallbacks.addRawOrThrow(this, requestCode, resultCallback)

    override fun <PRESENTER : AnyPresenter> registerPermissionResultCallback(
            requestCode: Int, onResult: ParcelFunction2<PRESENTER, Collection<String>, Unit>
    ) = resultCallbacks.addPermissionOrThrow(this, requestCode, onResult)

    override fun <PRESENTER : AnyPresenter> startActivity(
            intent: Intent, requestCode: Int, onResult: ParcelFunction3<PRESENTER, Int, Intent?, Unit>, options: Bundle?
    ) {
        resultCallbacks.addRawOrThrow(this, requestCode, onResult)
        startActivityForResult(intent, requestCode, options)
    }

    override val hasTarget: Boolean get() = targetFragment != null

    override fun deliverResult(obj: RET) {
        targetFragment.onActivityResult(
                targetRequestCode, Activity.RESULT_OK, Intent().also { it.putExtra("data", obj) }
        )
    }

    override fun deliverCancellation() {
        targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
    }


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
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("res cbs", resultCallbacks)
        outState.putParcelable("presenter", presenter!!.saveState())
    }

    override fun toString(): String = toString(super.toString(), presenter)

}

typealias ConsumerSupportBottomSheetDialogFragment<ARG> = SupportBottomSheetDialogFragment<ARG, ParcelUnit>
typealias SupplierSupportBottomSheetDialogFragment<RET> = SupportBottomSheetDialogFragment<ParcelUnit, RET>
typealias ActionSupportBottomSheetDialogFragment = SupportBottomSheetDialogFragment<ParcelUnit, ParcelUnit>
