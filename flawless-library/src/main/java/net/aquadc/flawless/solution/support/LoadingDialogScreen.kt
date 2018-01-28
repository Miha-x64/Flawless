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
import net.aquadc.flawless.solution.DataSource
import net.aquadc.flawless.solution.LoadingResult


class LoadingDialogScreen<ARG : Parcelable, LR_RET : Parcelable>(
        @param:StyleRes private val theme: Int = 0,
        private val provideSource: (ARG) -> DataSource<LR_RET>,
        private val title: CharSequenceSource,
        private val cancelable: Boolean = false,
        private val onLoad: (LoadingResult<LR_RET>, DialogFragment) -> Unit = { _, f -> f.dismiss() }
) : StatelessSupportDialogFragScreen<ARG, LoadingResult<LR_RET>> {

    private var source: DataSource<LR_RET>? = null

    override fun onCreate(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, arg: ARG, state: ParcelUnit?) {
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

    override fun createView(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, parent: Context, arg: ARG, state: ParcelUnit?): Dialog =
            ProgressDialog(parent, theme).apply {
                setTitle(title.get(host.resources))
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
            }

    override fun onViewCreated(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>, view: Dialog, arg: ARG, state: ParcelUnit?) {
        returnValue?.let {
            onLoad(it, host)
        }
    }

    override fun onViewDestroyed(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>) {
    }

    override fun saveState(): ParcelUnit = ParcelUnit

    override fun onDestroy(host: SupportDialogFragment<ARG, LoadingResult<LR_RET>>) {
        source!!.cancel()
        source = null
    }

    override var returnValue: LoadingResult<LR_RET>? = null
        private set

}
