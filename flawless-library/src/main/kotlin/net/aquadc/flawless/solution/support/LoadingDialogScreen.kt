package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Parcelable
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessSupportDialogFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.solution.CharSequenceSource
import net.aquadc.flawless.solution.ParcelFuture
import net.aquadc.flawless.solution.ParcelResult

/**
 * Shows a [ProgressDialog] and downloads some data.
 */
class LoadingDialogScreen<in ARG : Parcelable, LR_RET : Parcelable>(
        @param:StyleRes private val theme: Int = 0,
        private val provideSource: (ARG) -> ParcelFuture<LR_RET>,
        private val title: CharSequenceSource,
        private val cancelable: Boolean = false,
        private val onLoad: (ParcelResult<LR_RET>, DialogFragment) -> Unit = { _, f -> f.dismiss() }
) : StatelessSupportDialogFragScreen<ARG, ParcelResult<LR_RET>> {

    private var source: ParcelFuture<LR_RET>? = null

    override fun onCreate(host: SupportDialogFragment, arg: ARG, state: ParcelUnit?) {
        val source = provideSource(arg)
        source.subscribe {
            returnValue = it
            if (host.isAdded) {
                onLoad(it, host)
            }
        }
        this.source = source

        host.isCancelable = cancelable
    }

    override fun createView(host: SupportDialogFragment, parent: Context, arg: ARG, state: ParcelUnit?): Dialog =
            ProgressDialog(parent, theme).apply {
                setTitle(title.get(host.resources))
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
            }

    override fun onViewCreated(host: SupportDialogFragment, view: Dialog, arg: ARG, state: ParcelUnit?) {
        returnValue?.let {
            onLoad(it, host)
        }
    }

    override fun onViewDestroyed(host: SupportDialogFragment) {
    }

    override fun saveState(): ParcelUnit = ParcelUnit

    override fun onDestroy(host: SupportDialogFragment) {
        source!!.cancel()
        source = null
    }

    override var returnValue: ParcelResult<LR_RET>? = null
        private set

}
