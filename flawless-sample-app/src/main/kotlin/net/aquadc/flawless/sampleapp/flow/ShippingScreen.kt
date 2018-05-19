package net.aquadc.flawless.sampleapp.flow

import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.StatelessSupportFragScreen
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


class ShippingScreen(
        private val back: (Fragment) -> Unit
) : StatelessSupportFragScreen<@ParameterName("itemNo") ParcelInt, @ParameterName("shippingAddress") ParcelString> {

    override fun onCreate(host: SupportFragment, arg: ParcelInt, state: ParcelUnit?) {
    }

    override fun createView(host: SupportFragment, parent: ViewGroup, arg: ParcelInt, state: ParcelUnit?): View = host.UI {

        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER

            val addressInput = editText {
                id = 1
                inputType = InputType.TYPE_CLASS_TEXT
                hint = "Shipping address for item #${arg.value}"
            }.lparams(matchParent, wrapContent) {
                margin = dip(16)
            }

            button("Next") {
                setOnClickListener {
                    val text = addressInput.text.toString()
                    if (text.isNotBlank()) {
                        returnValue = ParcelString(text)
                        back(host)
                    }
                }
            }.lparams(wrapContent, wrapContent)

        }

    }.view

    override fun onViewCreated(host: SupportFragment, view: View, arg: ParcelInt, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportFragment) {
    }

    override fun onDestroy(host: SupportFragment) {
    }

    override var returnValue: ParcelString? = null
        private set

}
