package net.aquadc.flawless.sampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.extension.deliverCancellation
import net.aquadc.flawless.extension.deliverResult
import net.aquadc.flawless.implementMe.StatelessV4DialogFragPresenter
import net.aquadc.flawless.implementMe.V4DialogFragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.dip

class DialogPresenter : StatelessV4DialogFragPresenter<ParcelString, ParcelString> {

    private lateinit var host: MvpV4DialogFragment<*>

    override fun onCreate(host: MvpV4DialogFragment<ParcelString>, arg: ParcelString, state: ParcelUnit?) {
        host.onCancel = {
            host.deliverCancellation()
        }
    }

    override fun createView(host: MvpV4DialogFragment<ParcelString>, parent: Context, arg: ParcelString, state: ParcelUnit?): Dialog {
        this.host = host

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

    override fun onViewCreated(host: MvpV4DialogFragment<ParcelString>, view: Dialog, arg: ParcelString, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: MvpV4DialogFragment<ParcelString>) {
    }

    override fun onDetach() {
    }

}
