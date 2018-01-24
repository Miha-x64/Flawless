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
import net.aquadc.flawless.androidView.util.FragmentExchange
import net.aquadc.flawless.androidView.util.VisibilityStateListeners
import net.aquadc.flawless.implementMe.AnyPresenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.SupportFragPresenter
import net.aquadc.flawless.implementMe.VisibilityStateListener
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.AnyPresenterTag
import net.aquadc.flawless.tag.SupportFragPresenterTag

class SupportFragment<in ARG : Parcelable, RET : Parcelable>
    : Fragment, Host<RET>, PresenterFactory {

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


    private var _exchange: FragmentExchange<RET>? = null
    override val exchange: Host.Exchange<RET>
        get() = _exchange ?: FragmentExchange<RET>(this).also { _exchange = it }


    // own code


    private var presenter: SupportFragPresenter<ARG, RET, Parcelable>? = null
    private var isStateSaved = false

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

        _exchange = savedInstanceState?.getParcelable<FragmentExchange<RET>>("res cbs")?.also { it.fragment = this }
        presenter!!.onCreate(this, arg, savedInstanceState?.getParcelable("presenter"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            presenter!!.createView(this, container, arg, savedInstanceState?.getParcelable("presenter"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter!!.onViewCreated(this, view!!, arg, savedInstanceState?.getParcelable("presenter"))
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
        outState.putParcelable("presenter", presenter!!.saveState())
        isStateSaved = true
    }

    override fun onDestroyView() {
        visibilityState = VisibilityState.Uninitialized
        presenter!!.onViewDestroyed(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        val presenter = presenter!!
        if (isFinishing(isStateSaved) && targetFragment != null && !targetFragment.isFinishing(/*!*/isStateSaved)) {
            exchange.deliver(presenter.returnValue)
        }

        presenter.onDestroy(this)
        this.presenter = null
        _exchange?.fragment = null
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
        if (_exchange?.deliverResult(presenter!!, requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        _exchange?.deliverPermissionResult(presenter!!, requestCode, permissions, grantResults)
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
