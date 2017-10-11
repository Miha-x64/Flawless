package net.aquadc.flawless.androidView

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.V4DialogFragPresenter
import net.aquadc.flawless.tag.V4DialogFragPresenterTag

class MvpDialogFragmentV4<ARG : Parcelable> : DialogFragment {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: V4DialogFragPresenterTag<ARG, *>, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putParcelable("tag", tag)
            putParcelable("arg", arg)
        })
    }

    private val tag
        get() = arguments.getParcelable<V4DialogFragPresenterTag<ARG, Parcelable>>("tag")

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    private var presenter: V4DialogFragPresenter<ARG, Parcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter =
                (parentFragment as PresenterFactory? ?: activity as PresenterFactory).createPresenter(tag)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            presenter!!.createView(this, context, arguments.getParcelable<ARG>("arg"))

    override fun onDestroy() {
        presenter!!.detach()
        presenter = null
        super.onDestroy()
    }

    // dirty hack from https://stackoverflow.com/a/12434038
    override fun onDestroyView() {
        if (retainInstance) {
            dialog?.setDismissMessage(null)
        }
        super.onDestroyView()
    }


    // todo: result callbacks, as in MvpFragmentV4

}
