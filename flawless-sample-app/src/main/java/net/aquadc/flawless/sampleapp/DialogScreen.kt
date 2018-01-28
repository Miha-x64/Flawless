package net.aquadc.flawless.sampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessSupportDialogFragScreen
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.dip

class DialogScreen : StatelessSupportDialogFragScreen<ParcelString, ParcelString> {

    override fun onCreate(host: SupportDialogFragment<ParcelString, ParcelString>, arg: ParcelString, state: ParcelUnit?) {
    }

    override fun createView(
            host: SupportDialogFragment<ParcelString, ParcelString>, parent: Context,
            arg: ParcelString, state: ParcelUnit?
    ): Dialog {
        val view = AppCompatEditText(parent).also {
            it.id = 1
            it.layoutParams = ViewGroup.MarginLayoutParams(it.dip(16), it.dip(16))
        }

        return AlertDialog.Builder(parent)
                .setTitle(arg.value)
                .setView(view)
                .setPositiveButton("Ok") { _, _ ->
                    returnValue = ParcelString(view.text.toString())
                }
                .setNegativeButton("Cancel", null)
                .create()
    }

    override fun onViewCreated(host: SupportDialogFragment<ParcelString, ParcelString>, view: Dialog, arg: ParcelString, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportDialogFragment<ParcelString, ParcelString>) {
    }

    override fun onDestroy(host: SupportDialogFragment<ParcelString, ParcelString>) {
    }

    override var returnValue: ParcelString? = null
        private set

}
