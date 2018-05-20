package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Parcelable
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.screen.StatelessSupportDialogFragScreen
import net.aquadc.flawless.solution.CharSequenceSource
import net.aquadc.flawless.solution.ParcelFuture
import net.aquadc.flawless.solution.ParcelResult
import net.aquadc.flawless.screen.StatelessScreenArgs

/**
 * Shows a [ProgressDialog] and evaluates a [source] feature.
 */
class LoadingDialogScreen<in ARG : Parcelable, LR_RET : Parcelable>(
        private val req: StatelessScreenArgs<ARG, SupportDialogFragment>,
        @param:StyleRes private val theme: Int = 0,
        // force use of positional arguments ;)
        source: ParcelFuture<LR_RET>,
        private val title: CharSequenceSource,
        private val cancelable: Boolean = false,
        private val onLoad: (ParcelResult<LR_RET>, DialogFragment) -> Unit = { _, f -> f.dismiss() }
) : StatelessSupportDialogFragScreen<ARG, ParcelResult<LR_RET>> {

    private var source: ParcelFuture<LR_RET>? = source

    init {
        source.subscribe {
            this.source = null // we're done, nothing to cancel, free it
            returnValue = it
            if (req.host.isAdded) {
                onLoad(it, req.host)
            }
        }

        req.host.isCancelable = cancelable
    }

    override fun createView(parent: Context): Dialog  =
            ProgressDialog(parent, theme).apply {
                setTitle(title.get(req.host.resources))
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
            }

    override fun viewAttached(view: Dialog) {
        returnValue?.let {
            onLoad(it, req.host)
        }
    }

    override fun disposeView() {
    }

    override fun destroy() {
        source?.let {
            it.cancel()
            source = null
        }
    }

    override var returnValue: ParcelResult<LR_RET>? = null
        private set

}
