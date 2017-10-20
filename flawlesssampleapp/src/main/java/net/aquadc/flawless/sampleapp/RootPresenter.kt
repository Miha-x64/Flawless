package net.aquadc.flawless.sampleapp

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.MvpV4DialogFragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.extension.willStartForResult
import net.aquadc.flawless.implementMe.V4FragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.parcel.pureParcelFunction2
import net.aquadc.flawless.tag.V4DialogFragPresenterTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class RootPresenter(
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: V4DialogFragPresenterTag<ParcelString, ParcelString, *>
) : V4FragPresenter<ParcelUnit, ParcelUnit> {

    private lateinit var host: MvpV4Fragment<ParcelUnit>
    private lateinit var input: EditText
    private lateinit var output: TextView

    override fun createView(host: MvpV4Fragment<ParcelUnit>, parent: ViewGroup?, argument: ParcelUnit): View {
        this.host = host

        return host.UI {
            verticalLayout {
                lparams(matchParent, matchParent)
                gravity = Gravity.CENTER_VERTICAL

                input = editText {
                    id = 1
                    hint = "Question"
                }

                output = textView {
                    id = 2
                    freezesText = true
                }

                button {
                    id = 3
                    text = "Ask"
                    setOnClickListener {
                        openDialog()
                    }
                }
            }
        }.view
    }

    override fun onViewCreated(host: MvpV4Fragment<ParcelUnit>, view: View, argument: ParcelUnit) {
    }

    private fun openDialog() {
        val dialog =
                MvpV4DialogFragment(questionPresenterTag, ParcelString(input.text.toString()))

        host.willStartForResult(dialog, 1, pureParcelFunction2(RootPresenter::gotResponse))
        openDialog(host, dialog)
    }

    private fun gotResponse(string: ParcelString) {
        output.text = string.value
    }

    override fun detach() {
    }

}
