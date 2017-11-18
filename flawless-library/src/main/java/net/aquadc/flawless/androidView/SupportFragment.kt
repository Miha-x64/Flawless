package net.aquadc.flawless.androidView

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
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.SupportFragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelFunction1
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.parcel.ParcelFunction3
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.SupportFragPresenterTag

class SupportFragment<in ARG : Parcelable, out RET : Parcelable> : Fragment, PresenterFactory {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: SupportFragPresenterTag<ARG, RET, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag: SupportFragPresenterTag<ARG, RET, Presenter<ARG, RET, SupportFragment<ARG, RET>, ViewGroup?, View, *>>
        get() = arguments.getParcelable("tag")

    private val arg: ARG
        get() = arguments.getParcelable("arg")

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    var visibilityState = VisibilityState.Uninitialized
        private set(new) {
            if (field != new) {
                val old = field
                field = new
                val listeners = visibilityStateListeners
                if (listeners != null) {
                    val copy = listeners.toTypedArray()
                    for (listener in copy) {
                        listener.onVisibilityStateChanged(this, old, new)
                    }
                }
            }
        }

    private var visibilityStateListeners: MutableList<VisibilityStateListener>? = null

    /**
     * Adds visibility state listeners.
     */
    fun addVisibilityStateListener(listener: VisibilityStateListener) {
        var listeners = visibilityStateListeners
        if (listeners == null) {
            listeners = ArrayList(1)
            visibilityStateListeners = listeners
        }

        listeners.add(listener)
    }

    /**
     * Removes visibility state listeners.
     */
    fun removeVisibilityStateListener(listener: VisibilityStateListener) {
        visibilityStateListeners?.remove(listener)
    }

    private var presenter: SupportFragPresenter<ARG, RET, Parcelable>? = null

    /**
     * Shouldn't ever exist, but may be necessary for interop.
     */
    @Deprecated("After refactoring, you may never need it.")
    fun getPresenter(): SupportFragPresenter<*, *, *>? = presenter as SupportFragPresenter<*, *, *>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (presenter == null) { // may be re-attached
            val presenter =
                    findPresenterFactory().createPresenter(tag)
            tag.checkPresenter(presenter)
            this.presenter = presenter as SupportFragPresenter<ARG, RET, Parcelable> // erase state type
            presenter.onAttach(this)
        }
    }

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


    private var _resultCallbacks: ResultCallbacks? = null

    private val resultCallbacks: ResultCallbacks
        get() = _resultCallbacks ?: ResultCallbacks().also { _resultCallbacks = it }

    internal fun <PRESENTER : Presenter<*, *, *, *, *, *>, RET> registerResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction2<PRESENTER, RET, Unit>,
            cancellationCallback: ParcelFunction1<PRESENTER, Unit>
    ) {
        resultCallbacks.addOrThrow(this, requestCode, resultCallback, cancellationCallback)
    }

    fun <PRESENTER : Presenter<*, *, *, *, *, *>> registerRawResultCallback(
            requestCode: Int,
            resultCallback: ParcelFunction3<PRESENTER, Int, Intent?, Unit>
    ) {
        resultCallbacks.addRawOrThrow(this, requestCode, resultCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCallbacks?.deliverResult(presenter!!, requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("res cbs", resultCallbacks)
        outState.putParcelable("presenter", presenter!!.saveState())
    }

    override fun createPresenter(tag: PresenterTag<*, *, *, *, *, *>): Presenter<*, *, *, *, *, *> {
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
