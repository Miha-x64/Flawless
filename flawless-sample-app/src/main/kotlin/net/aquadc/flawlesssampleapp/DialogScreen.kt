package net.aquadc.flawlesssampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.screen.StatelessSupportDialogFragScreen
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.screen.StatelessScreenArgs
import org.jetbrains.anko.dip


class DialogScreen(
        private val req: StatelessScreenArgs<ParcelString, SupportDialogFragment>
) : StatelessSupportDialogFragScreen<ParcelString, ParcelString> {

    override fun createView(parent: Context): Dialog {
        val view = AppCompatEditText(parent).also {
            it.id = 1
            it.layoutParams = ViewGroup.MarginLayoutParams(it.dip(16), it.dip(16))
        }

        return AlertDialog.Builder(parent)
                .setTitle(req.arg.value)
                .setView(view)
                .setPositiveButton("Ok") { _, _ ->
                    returnValue = ParcelString(view.text.toString())
                }
                .setNegativeButton("Cancel", null)
                .create()
    }

    override fun viewAttached(view: Dialog) {
    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

    override var returnValue: ParcelString? = null
        private set

}
