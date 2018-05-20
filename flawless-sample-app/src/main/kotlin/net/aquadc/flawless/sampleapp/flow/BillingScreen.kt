package net.aquadc.flawless.sampleapp.flow

import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.screen.StatelessSupportFragScreen
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.screen.StatelessScreenArgs
import org.jetbrains.anko.*


class BillingScreen(
        private val args: StatelessScreenArgs<ParcelInt, SupportFragment>,
        private val back: (Fragment) -> Unit
) : StatelessSupportFragScreen<@ParameterName("itemCount") ParcelInt, @ParameterName("billingAddress") ParcelString> {

    override fun createView(parent: ViewGroup): View = parent.context.UI {

        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER

            val itemCount = args.arg.value

            val addressInput = editText {
                id = 1
                inputType = InputType.TYPE_CLASS_TEXT
                hint = if (itemCount == 1) "Billing address" else "Billing address for $itemCount items"
            }.lparams(matchParent, wrapContent) {
                margin = dip(16)
            }

            button("Next") {
                setOnClickListener {
                    val text = addressInput.text.toString()
                    if (text.isNotBlank()) {
                        returnValue = ParcelString(text)
                        back(args.host)
                    }
                }
            }.lparams(wrapContent, wrapContent)

        }

    }.view

    override fun viewAttached(view: View) {
    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

    override var returnValue: ParcelString? = null
        private set

}
