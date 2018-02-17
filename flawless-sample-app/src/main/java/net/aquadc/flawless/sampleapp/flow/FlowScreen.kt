package net.aquadc.flawless.sampleapp.flow

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.StatelessActionSupportFragScreen
import net.aquadc.flawless.parcel.*
import net.aquadc.flawless.tag.SupportFragScreenTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


class FlowScreen(
        private val shippingScreenTag: SupportFragScreenTag<ParcelInt, ParcelString, *>,
        private val billingScreenTag: SupportFragScreenTag<ParcelInt, ParcelString, *>,
        private val openFragment: (Fragment, Fragment) -> Unit
) : StatelessActionSupportFragScreen {

    private lateinit var flowHost: SupportFragment
    private var continuation: Continuation<*>? = null

    // It's important not to capture views
    private lateinit var output: TextView

    override fun onCreate(host: SupportFragment, arg: ParcelUnit, state: ParcelUnit?) {
        this.flowHost = host
    }

    override fun createView(host: SupportFragment, parent: ViewGroup, arg: ParcelUnit, state: ParcelUnit?): View = host.UI {
        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER
            padding = dip(16)

            textView("You are going to begin coroutine-based (experimental) toy checkout flow.")
                    .lparams(matchParent, wrapContent)

            val picker = editText {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = "How many items are you going to buy?"
            }.lparams(matchParent, wrapContent)

            val beginButton = button("Begin").lparams(wrapContent, wrapContent)

            output = textView("Buy something!!") {
                freezesText = true
            }.lparams(matchParent, wrapContent)

            beginButton.setOnClickListener {
                launch(UI) {
                    val itemCount = picker.text.toString().toIntOrNull()
                    if (itemCount == null || itemCount < 0) return@launch

                    val itemNumbers = 1 .. itemCount
                    val shippingAddresses = itemNumbers.map { orderNo ->
                        shippingScreenTag.openAndAwaitResult(ParcelInt(orderNo)).value
                        // ask user where he/she wants each item to be shipped
                    }

                    val billingAddress =
                            billingScreenTag.openAndAwaitResult(ParcelInt(itemCount)).value
                    // ...and where we should write a cheque to.

                    output.text =
                            if (itemCount == 1) "Item will be shipped to ${shippingAddresses.single()} and billed to $billingAddress"
                            else shippingAddresses.joinToString(
                                    prefix = "Items will be shipped to ",
                                    postfix = " accordingly. All $itemCount items will be billed to $billingAddress."
                            )
                }
            }
        }
    }.view

    private suspend fun <ARG : Parcelable, RET : Parcelable> SupportFragScreenTag<ARG, RET, *>.openAndAwaitResult(
            arg: ARG
    ): RET {
        check(continuation == null)
        val fragment = SupportFragment(this, arg)
        fragment.setTargetFragment(flowHost, 1)
        flowHost.exchange.registerRawResultCallback(this@FlowScreen, 1, pureParcelFunction3(FlowScreen::onActivityResult))
        openFragment(flowHost, fragment)
        return suspendCoroutine {
            continuation = it
        }
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        val con = continuation
                ?: flowHost.toast("Oops! I've missed it. App process was killed, and Continuations are not serializable.").let { return }

        continuation = null

        when (resultCode) {
            Activity.RESULT_OK -> (con as Continuation<Parcelable>).resume(data!!.getParcelableExtra("data"))
            Activity.RESULT_CANCELED -> con.resumeWithException(CancellationException("RESULT_CANCELED"))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onViewCreated(host: SupportFragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportFragment) {
    }

    override fun onDestroy(host: SupportFragment) {
    }

}
