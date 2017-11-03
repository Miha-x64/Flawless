package net.aquadc.flawless.solution

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Parcelable
import android.support.annotation.StyleRes
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.extension.deliverCancellation
import net.aquadc.flawless.extension.deliverResult
import net.aquadc.flawless.implementMe.StatelessV4DialogFragPresenter
import net.aquadc.flawless.parcel.ParcelUnit

class V4LoaderDialogPresenter<ARG : Parcelable, LR_RET : Parcelable>(
        @param:StyleRes private val theme: Int = 0,
        private val provideSource: (ARG) -> DataSource<LR_RET>,
        private val title: CharSequence = "",
        private val cancelable: Boolean = false
) : StatelessV4DialogFragPresenter<ARG, LoadingResult<LR_RET>> {

    private var source: DataSource<LR_RET>? = null
    private var result: LoadingResult<LR_RET>? = null

    override fun onCreate(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>, arg: ARG, state: ParcelUnit?) {
        val source = provideSource(arg)
        source.subscribe {
            result = it
            deliver(host)
        }
        this.source = source

        host.isCancelable = cancelable
    }

    override fun createView(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>, parent: Context, arg: ARG, state: ParcelUnit?): Dialog =
            ProgressDialog(parent, theme).apply {
                setTitle(title)
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
                if (cancelable) {
                    setOnCancelListener { host.deliverCancellation() }
                }
            }

    override fun onViewCreated(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>, view: Dialog, arg: ARG, state: ParcelUnit?) {
        if (result != null) deliver(host)
    }

    private fun deliver(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>) {
        if (host.isAdded) {
            host.dismiss()
            host.deliverResult(result!!)
        }
    }

    override fun onViewDestroyed(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>) {
    }

    override fun saveState(): ParcelUnit = ParcelUnit

    override fun onDestroy(host: MvpV4DialogFragment<ARG, LoadingResult<LR_RET>>) {
        source!!.cancel()
        source = null
    }

}
