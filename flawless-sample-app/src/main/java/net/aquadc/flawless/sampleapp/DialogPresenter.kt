package net.aquadc.flawless.sampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportDialogFragment
import net.aquadc.flawless.implementMe.StatelessSupportDialogFragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.dip

class DialogPresenter : StatelessSupportDialogFragPresenter<ParcelString, ParcelString> {

    override fun onCreate(host: SupportDialogFragment<ParcelString, ParcelString>, arg: ParcelString, state: ParcelUnit?) {
        host.onCancel = {
            host.deliverCancellation()
        }
    }

    override fun createView(host: SupportDialogFragment<ParcelString, ParcelString>, parent: Context, arg: ParcelString, state: ParcelUnit?): Dialog {
        val view = AppCompatEditText(parent).also {
            it.id = 1
            it.layoutParams = ViewGroup.MarginLayoutParams(it.dip(16), it.dip(16))
        }

        return AlertDialog.Builder(parent)
                .setTitle(arg.value)
                .setView(view)
                .setPositiveButton("Ok", { _, _ ->
                    host.deliverResult(ParcelString(view.text.toString()))
                })
                .setNegativeButton("Cancel", { _, _ ->
                    host.deliverCancellation()
                })
                .create()
    }

    override fun onViewCreated(host: SupportDialogFragment<ParcelString, ParcelString>, view: Dialog, arg: ParcelString, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportDialogFragment<ParcelString, ParcelString>) {
    }

    override fun onDestroy(host: SupportDialogFragment<ParcelString, ParcelString>) {
    }

}
