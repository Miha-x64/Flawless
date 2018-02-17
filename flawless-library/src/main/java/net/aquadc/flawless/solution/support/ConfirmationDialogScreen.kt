package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessActionSupportDialogFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.solution.CancelCharSequence
import net.aquadc.flawless.solution.CharSequenceSource
import net.aquadc.flawless.solution.OkCharSequence

/**
 * Asks user if he/she agrees with something.
 * Returns [ParcelUnit] if positive button clicked, `null` otherwise.
 */
class ConfirmationDialogScreen(
        @param:StyleRes private val theme: Int = 0,
        private val title: CharSequenceSource,
        private val message: CharSequenceSource,
        private val positiveText: CharSequenceSource = OkCharSequence,
        private val negativeText: CharSequenceSource = CancelCharSequence,
        private val cancelable: Boolean = true
) : StatelessActionSupportDialogFragScreen {

    override fun onCreate(host: SupportDialogFragment, arg: ParcelUnit, state: ParcelUnit?) {
        host.isCancelable = cancelable
    }

    override fun createView(host: SupportDialogFragment, parent: Context, arg: ParcelUnit, state: ParcelUnit?): Dialog {
        val res = host.resources
        return AlertDialog.Builder(parent, theme)
                .setTitle(title.get(res))
                .setMessage(message.get(res))
                .setCancelable(cancelable)
                .setPositiveButton(positiveText.get(res)) { _, _ -> returnValue = ParcelUnit }
                .setNegativeButton(negativeText.get(res), null)
                .create()
    }

    override fun onViewCreated(host: SupportDialogFragment, view: Dialog, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportDialogFragment) {
    }


    override fun onDestroy(host: SupportDialogFragment) {
    }

    override var returnValue: ParcelUnit? = null
        private set

}
