package net.aquadc.flawless.solution.support

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.screen.StatelessActionSupportDialogFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.solution.CancelCharSequence
import net.aquadc.flawless.solution.CharSequenceSource
import net.aquadc.flawless.solution.OkCharSequence
import net.aquadc.flawless.screen.StatelessActionScreenArgs

/**
 * Asks user if he/she agrees with a question given as [message].
 * Returns [ParcelUnit] if positive button clicked, `null` otherwise.
 */
class ConfirmationDialogScreen(
        private val req: StatelessActionScreenArgs<SupportDialogFragment>,
        @param:StyleRes private val theme: Int = 0,
        // force use of positional arguments ;)
        private val title: CharSequenceSource,
        private val message: CharSequenceSource,
        private val positiveText: CharSequenceSource = OkCharSequence,
        private val negativeText: CharSequenceSource = CancelCharSequence,
        private val cancelable: Boolean = true
) : StatelessActionSupportDialogFragScreen {

    init {
        req.host.isCancelable = cancelable
    }

    override fun createView(parent: Context): Dialog = req.host.resources.let { res ->
        AlertDialog.Builder(parent, theme)
                .setTitle(title.get(res))
                .setMessage(message.get(res))
                .setCancelable(cancelable)
                .setPositiveButton(positiveText.get(res)) { _, _ -> returnValue = ParcelUnit }
                .setNegativeButton(negativeText.get(res), null)
                .create()
    }

    override fun viewAttached(view: Dialog) {
    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

    override var returnValue: ParcelUnit? = null
        private set

}
