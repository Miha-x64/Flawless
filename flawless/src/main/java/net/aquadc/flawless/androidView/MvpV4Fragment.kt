package net.aquadc.flawless.androidView

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.util.ResultCallbacks
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.V4FragPresenter
import net.aquadc.flawless.parcel.ParcelFunction2
import net.aquadc.flawless.tag.V4FragPresenterTag

class MvpV4Fragment<ARG : Parcelable> : Fragment {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: V4FragPresenterTag<ARG, *, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag: V4FragPresenterTag<ARG, Parcelable, Presenter<ARG, Parcelable, MvpV4Fragment<ARG>, ViewGroup?, View>>
        get() = arguments.getParcelable("tag")

    private val arg: ARG
        get() = arguments.getParcelable("arg")

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    private var presenter: V4FragPresenter<ARG, Parcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val presenter =
                (parentFragment as PresenterFactory? ?: activity as PresenterFactory).createPresenter(tag)
        tag.checkPresenter(presenter)
        this.presenter = presenter

        resultCallbacks = savedInstanceState?.getParcelable("res cbs")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            presenter!!.createView(this, container, arg)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) =
            presenter!!.onViewCreated(this, view!!, arg)

    override fun onDestroy() {
        presenter!!.detach()
        presenter = null
        super.onDestroy()
    }


    private var resultCallbacks: ResultCallbacks? = null
    fun <PRESENTER : Presenter<ARG, *, *, *, *>, RET> registerResultCallback(
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
    }

}
