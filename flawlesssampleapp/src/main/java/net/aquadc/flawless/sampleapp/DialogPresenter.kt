package net.aquadc.flawless.sampleapp

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.ViewGroup
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.androidView.MvpDialogFragmentV4
import net.aquadc.flawless.extension.deliverCancellation
import net.aquadc.flawless.extension.deliverResult
import net.aquadc.flawless.parcel.ParcelString
import org.jetbrains.anko.dip

class DialogPresenter : Presenter<ParcelString, ParcelString, MvpDialogFragmentV4<ParcelString>, Context, Dialog> {

    private lateinit var host: MvpDialogFragmentV4<*>
    private var delivered = false

    override fun createView(host: MvpDialogFragmentV4<ParcelString>, parent: Context, argument: ParcelString): Dialog {
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

    override fun detach() {
        if (!delivered) {
            host.deliverCancellation()
        }
    }

}
