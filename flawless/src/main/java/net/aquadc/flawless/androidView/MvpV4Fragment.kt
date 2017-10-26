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
import net.aquadc.flawless.implementMe.V4FragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.V4FragPresenterTag

class MvpV4Fragment<ARG : Parcelable> : Fragment, PresenterFactory {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: V4FragPresenterTag<ARG, *, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag: V4FragPresenterTag<ARG, Parcelable, Presenter<ARG, Parcelable, MvpV4Fragment<ARG>, ViewGroup?, View, *>>
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

    private var presenter: V4FragPresenter<ARG, Parcelable, Parcelable>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val presenter =
                findPresenterFactory().createPresenter(tag)
        tag.checkPresenter(presenter)
        this.presenter = presenter as V4FragPresenter<ARG, Parcelable, Parcelable> // erase state type
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        resultCallbacks = savedInstanceState?.getParcelable("res cbs")
        presenter!!.onCreate(this, arg, savedInstanceState?.getParcelable("presenter"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            presenter!!.createView(this, container, arg, savedInstanceState?.getParcelable("presenter"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter!!.onViewCreated(this, view!!, arg, savedInstanceState?.getParcelable("presenter"))
        // you can set visibilityStateListener in onViewCreated, and will receive an update then
        updateVisibilityState()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        updateVisibilityState()
    }

    override fun onDestroyView() {
        presenter!!.onViewDestroyed(this)
        updateVisibilityState()
        super.onDestroyView()
    }

    private fun updateVisibilityState() {
        visibilityState = when {
            isAdded && userVisibleHint -> VisibilityState.Visible
            isAdded -> VisibilityState.Invisible
            else -> VisibilityState.Uninitialized
        }
    }

    override fun onDestroy() {
        presenter!!.onDetach()
        presenter = null
        super.onDestroy()
    }


    private var resultCallbacks: ResultCallbacks? = null
    fun <PRESENTER : Presenter<ARG, *, *, *, *, *>, RET> registerResultCallback(
            requestCode: Int,
            callback: ParcelFunction2<PRESENTER, RET, Unit>
    ) {
        (resultCallbacks ?: ResultCallbacks().also { resultCallbacks = it })
                .addOrThrow(requestCode, callback)
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

    override fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V, *>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER {
        val presenter = presenter

        if (presenter == null)
            throw IllegalStateException("Presenter is not attached.")

        if (presenter !is PresenterFactory)
            throw UnsupportedOperationException("Presenter does not implement PresenterFactory.")

        return presenter.createPresenter(tag)
    }

    override fun toString(): String = toString(super.toString(), presenter)

}
