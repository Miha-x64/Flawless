package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Parcelable
import android.support.annotation.StyleRes
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessSupportDialogFragPresenter
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.solution.DataSource
import net.aquadc.flawless.solution.LoadingResult

class LoadingDialogPresenter<ARG : Parcelable, LR_RET : Parcelable>(
        @param:StyleRes private val theme: Int = 0,
        private val provideSource: (ARG) -> DataSource<LR_RET>,
        private val title: CharSequence = "",
        private val cancelable: Boolean = false
) : StatelessSupportDialogFragPresenter<ARG, LoadingResult<LR_RET>> {

    private var source: DataSource<LR_RET>? = null
    private var result: LoadingResult<LR_RET>? = null

    override fun onCreate(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, arg: ARG, state: ParcelUnit?) {
        val source = provideSource(arg)
        source.subscribe {
            result = it
            deliver(host)
        }
        this.source = source

        host.isCancelable = cancelable
    }

    override fun createView(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, parent: Context, arg: ARG, state: ParcelUnit?): Dialog =
            ProgressDialog(parent, theme).apply {
                setTitle(title)
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
                if (cancelable) {
                    setOnCancelListener { host.exchange.deliverCancellation() }
                }
            }

    override fun onViewCreated(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, view: Dialog, arg: ARG, state: ParcelUnit?) {
        if (result != null) deliver(host)
    }

    private fun deliver(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>) {
        if (host.isAdded) {
            host.dismiss()
            host.exchange.deliverResult(result!!)
        }
    }

    override fun onViewDestroyed(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>) {
    }

    override fun saveState(): ParcelUnit = ParcelUnit

    override fun onDestroy(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>) {
        source!!.cancel()
        source = null
    }

}
