package net.aquadc.flawless.androidView

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.view.View
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.V4DialogFragPresenter
import net.aquadc.flawless.tag.V4DialogFragPresenterTag

class MvpV4DialogFragment<ARG : Parcelable> : DialogFragment {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: V4DialogFragPresenterTag<ARG, *, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag: V4DialogFragPresenterTag<ARG, Parcelable, Presenter<ARG, Parcelable, MvpV4DialogFragment<ARG>, Context, Dialog, *>>
        get() = arguments.getParcelable("tag")

    private val arg: ARG
        get() = arguments.getParcelable("arg")

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    var onCancel: (() -> Unit)? = null

    override fun onCancel(dialog: DialogInterface?) {
        onCancel?.invoke()
    }


    private var presenter: V4DialogFragPresenter<ARG, Parcelable, Parcelable>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val presenter =
                findPresenterFactory().createPresenter(tag)
        tag.checkPresenter(presenter)
        this.presenter = presenter as V4DialogFragPresenter<ARG, Parcelable, Parcelable> // erase state type
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter!!.onCreate(this, arg, savedInstanceState?.getParcelable("presenter"))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            presenter!!.createView(this, context, arg, savedInstanceState?.getParcelable("presenter"))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) =
            presenter!!.onViewCreated(this, dialog, arg, savedInstanceState?.getParcelable("presenter"))

    // dirty hack from https://stackoverflow.com/a/12434038
    override fun onDestroyView() {
        presenter!!.onViewDestroyed(this)
        if (retainInstance) {
            dialog?.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter!!.onDetach()
        presenter = null
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("presenter", presenter!!.saveState())
    }

    override fun toString(): String = toString(super.toString(), presenter)


    // todo: result callbacks, as in MvpV4Fragment

}
