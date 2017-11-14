package net.aquadc.flawless.androidView

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatDialogFragment
import android.view.View
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.SupportDialogFragPresenter
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.SupportDialogFragPresenterTag

class SupportDialogFragment<in ARG : Parcelable, out RET : Parcelable> : AppCompatDialogFragment {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: SupportDialogFragPresenterTag<ARG, RET, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag: SupportDialogFragPresenterTag<ARG, RET, Presenter<ARG, RET, SupportDialogFragment<ARG, RET>, Context, Dialog, *>>
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


    private var presenter: SupportDialogFragPresenter<ARG, RET, Parcelable>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (presenter == null) {
            val presenter =
                    findPresenterFactory().createPresenter(tag)
            tag.checkPresenter(presenter)
            this.presenter = presenter as SupportDialogFragPresenter<ARG, RET, Parcelable> // erase state type
            presenter.onAttach(this)
        }
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
        presenter!!.onDestroy(this)
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

typealias ConsumerSupportDialogFragment<ARG> = SupportDialogFragment<ARG, ParcelUnit>
typealias SupplierSupportDialogFragment<RET> = SupportDialogFragment<ParcelUnit, RET>
typealias ActionSupportDialogFragment = SupportDialogFragment<ParcelUnit, ParcelUnit>
