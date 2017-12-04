package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessActionSupportDialogFragPresenter
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.solution.CancelCharSequence
import net.aquadc.flawless.solution.CharSequenceSource
import net.aquadc.flawless.solution.OkCharSequence

class ConfirmationDialogPresenter(
        @param:StyleRes private val theme: Int = 0,
        private val title: CharSequenceSource,
        private val message: CharSequenceSource,
        private val positiveText: CharSequenceSource = OkCharSequence,
        private val negativeText: CharSequenceSource = CancelCharSequence,
        private val cancelable: Boolean = true
) : StatelessActionSupportDialogFragPresenter {

    override fun onCreate(host: SupportDialogFragment<ParcelUnit, ParcelUnit>, arg: ParcelUnit, state: ParcelUnit?) {
        host.isCancelable = cancelable
        host.onCancel = { host.exchange.deliverCancellation() }
    }

    override fun createView(host: SupportDialogFragment<ParcelUnit, ParcelUnit>, parent: Context, arg: ParcelUnit, state: ParcelUnit?): Dialog {
        val res = host.resources
        return AlertDialog.Builder(parent, theme)
                .setTitle(title.get(res))
                .setMessage(message.get(res))
                .setCancelable(cancelable)
                .setPositiveButton(positiveText.get(res)) { _, _ -> host.exchange.deliverResult(ParcelUnit) }
                .setNegativeButton(negativeText.get(res)) { _, _ -> host.exchange.deliverCancellation() }
                .also { if (cancelable) { it.setOnCancelListener { host.exchange.deliverCancellation() } } }
                .create()
    }

    override fun onViewCreated(host: SupportDialogFragment<ParcelUnit, ParcelUnit>, view: Dialog, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportDialogFragment<ParcelUnit, ParcelUnit>) {
    }


    override fun onDestroy(host: SupportDialogFragment<ParcelUnit, ParcelUnit>) {
    }

}
