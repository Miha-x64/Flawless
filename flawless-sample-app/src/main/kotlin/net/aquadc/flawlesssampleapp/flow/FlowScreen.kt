package net.aquadc.flawlesssampleapp.flow

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.screen.StatelessActionSupportFragScreen
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.pureParcelFunction
import net.aquadc.flawless.screen.StatelessActionScreenArgs
import net.aquadc.flawless.tag.SupportFragScreenTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.toast
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FlowScreen(
        private val args: StatelessActionScreenArgs<SupportFragment>,
        private val shippingScreenTag: SupportFragScreenTag<ParcelInt, ParcelString, *>,
        private val billingScreenTag: SupportFragScreenTag<ParcelInt, ParcelString, *>,
        private val openFragment: (Fragment, Fragment) -> Unit
) : StatelessActionSupportFragScreen {

    private var continuation: Continuation<*>? = null

    // It's important not to capture views
    private lateinit var output: TextView

    override fun createView(parent: ViewGroup): View = parent.context.UI {
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
                textSize = 24f
            }.lparams(matchParent, wrapContent)

            beginButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val itemCount = picker.text.toString().toIntOrNull()
                    if (itemCount == null || itemCount < 0) return@launch

                    val itemNumbers = 1 .. itemCount
                    val shippingAddresses = itemNumbers.map { orderNo ->
                        shippingScreenTag
                                .openAndAwaitResult(ParcelInt(orderNo)).value
                        // ask user where he/she wants each item to be shipped
                    }

                    val billingAddress =
                            billingScreenTag
                                    .openAndAwaitResult(ParcelInt(itemCount)).value
                    // ...and where we should send a cheque to.

                    output.text =
                            if (itemCount == 1) "Item will be shipped to ${shippingAddresses.single()} and billed to $billingAddress."
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
        val flowHost = args.host
        fragment.setTargetFragment(flowHost, 1)
        flowHost.exchange.registerRawResultCallback(this@FlowScreen, 1, pureParcelFunction(FlowScreen::onActivityResult))
        openFragment(flowHost, fragment)
        return suspendCoroutine {
            continuation = it
        }
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        val con = continuation
                ?: args.host.toast("Oops! I've missed it. App process was killed, and Continuations are not serializable.").let { return }

        continuation = null

        when (resultCode) {
            Activity.RESULT_OK -> (con as Continuation<Parcelable>).resume(data!!.getParcelableExtra("data")!!)
            Activity.RESULT_CANCELED -> con.resumeWithException(CancellationException("RESULT_CANCELED"))
            else -> throw IllegalArgumentException()
        }
    }

    override fun viewAttached(view: View) {
    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

}
