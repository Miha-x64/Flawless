package net.aquadc.flawless.androidView

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory

class MvpDialogFragmentV4<ARG : Parcelable> : DialogFragment {

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    constructor()

    constructor(tag: String, arg: ARG) {
        super.setArguments(Bundle(2).apply {
            putString("tag", tag)
            putParcelable("arg", arg)
        })
    }

    @Deprecated(message = "used by framework", level = DeprecationLevel.ERROR)
    override fun setArguments(args: Bundle) {
        if (arguments != null)
            throw IllegalStateException()

        super.setArguments(args)
    }


    private var presenter: Presenter<ARG, *, Fragment, Context, Dialog>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter =
                (parentFragment as PresenterFactory? ?: activity as PresenterFactory)
                        .createPresenter<ARG, Parcelable, Fragment, Context, Dialog>(arguments.getString("tag"))
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
