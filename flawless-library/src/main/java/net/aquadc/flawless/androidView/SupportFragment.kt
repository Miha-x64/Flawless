package net.aquadc.flawless.androidView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.VisibilityState
import net.aquadc.flawless.androidView.util.ResultCallbacks
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.*
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.AnyPresenterTag
import net.aquadc.flawless.tag.SupportFragPresenterTag

class SupportFragment<in ARG : Parcelable, RET : Parcelable>
    : Fragment, Host<RET>, Host.Exchange<RET>, PresenterFactory {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: SupportFragPresenterTag<ARG, RET, *>, arg: ARG) {
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


    // Host.Exchange impl


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


    // own code


    private var presenter: SupportFragPresenter<ARG, RET, Parcelable>? = null

    /**
     * Shouldn't ever exist, but may be necessary for interop.
     */
    @Deprecated("After refactoring, you may never need it.")
    fun getPresenter(): SupportFragPresenter<*, *, *>? = presenter as SupportFragPresenter<*, *, *>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (presenter == null) { // may be re-attached to new Activity
            val presenter =
                    findPresenterFactory().createPresenter(arguments.getParcelable("tag"))
            this.presenter = presenter as SupportFragPresenter<ARG, RET, Parcelable> // erase state type
            presenter.onAttach(this)
        }
    }

    private val arg: ARG get() = arguments.getParcelable("arg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        _resultCallbacks = savedInstanceState?.getParcelable("res cbs")
        presenter!!.onCreate(this, arg, savedInstanceState?.getParcelable("presenter"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            presenter!!.createView(this, container, arg, savedInstanceState?.getParcelable("presenter"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter!!.onViewCreated(this, view!!, arg, savedInstanceState?.getParcelable("presenter"))
        // you can set visibilityStateListener in onViewCreated, and will receive an update then
        visibilityState = if (userVisibleHint) VisibilityState.Visible else VisibilityState.Invisible
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded && view != null && visibilityState != VisibilityState.Uninitialized) {
            visibilityState = if (isVisibleToUser) VisibilityState.Visible else VisibilityState.Invisible
        }
    }

    override fun onDestroyView() {
        visibilityState = VisibilityState.Uninitialized
        presenter!!.onViewDestroyed(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter!!.onDestroy(this)
        presenter = null
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (_resultCallbacks?.deliverResult(presenter!!, requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        _resultCallbacks?.deliverPermissionResult(presenter!!, requestCode, permissions, grantResults)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("res cbs", resultCallbacks)
        outState.putParcelable("presenter", presenter!!.saveState())
    }

    override fun createPresenter(tag: AnyPresenterTag): AnyPresenter {
        val presenter = presenter

        if (presenter == null)
            throw IllegalStateException("Presenter is not attached.")

        if (presenter !is PresenterFactory)
            throw UnsupportedOperationException("Presenter $presenter does not implement PresenterFactory.")

        return presenter.createPresenter(tag)
    }

    override fun toString(): String = toString(super.toString(), presenter)

}

typealias ConsumerSupportFragment<ARG> = SupportFragment<ARG, ParcelUnit>
typealias SupplierSupportFragment<RET> = SupportFragment<ParcelUnit, RET>
typealias ActionSupportFragment = SupportFragment<ParcelUnit, ParcelUnit>
