package net.aquadc.flawless.sampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.extension.deliverCancellation
import net.aquadc.flawless.extension.deliverResult
import net.aquadc.flawless.implementMe.V4DialogFragPresenter
import net.aquadc.flawless.parcel.ParcelString
import org.jetbrains.anko.dip

class DialogPresenter : V4DialogFragPresenter<ParcelString, ParcelString> {

    private lateinit var host: MvpV4DialogFragment<*>
    private var delivered = false

    override fun createView(host: MvpV4DialogFragment<ParcelString>, parent: Context, argument: ParcelString): Dialog {
        this.host = host

        val view = AppCompatEditText(parent).also {
            it.id = 1
            it.layoutParams = ViewGroup.MarginLayoutParams(it.dip(16), it.dip(16))
        }

        return AlertDialog.Builder(parent)
                .setTitle(argument.value)
                .setView(view)
                .setPositiveButton("Ok", { _, _ ->
                    host.deliverResult(ParcelString(view.text.toString()))
                    delivered = true
                })
                .setNegativeButton("Cancel", null)
                .create()
    }

    override fun onViewCreated(host: MvpV4DialogFragment<ParcelString>, view: Dialog, argument: ParcelString) {
    }

    override fun detach() {
        if (!delivered) {
            host.deliverCancellation()
        }
    }

}
