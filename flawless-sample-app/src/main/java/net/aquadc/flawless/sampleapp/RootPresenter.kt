package net.aquadc.flawless.sampleapp

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.ActionSupportFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.extension.createDialogFragmentForResult
import net.aquadc.flawless.implementMe.StatelessActionSupportFragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.parcel.pureParcelFunction1
import net.aquadc.flawless.parcel.pureParcelFunction2
import net.aquadc.flawless.tag.SupplierSupportFragPresenterTag
import net.aquadc.flawless.tag.SupportDialogFragPresenterTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class RootPresenter(
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: SupportDialogFragPresenterTag<ParcelString, ParcelString, *>,
        private val pagerPresenterTag: SupplierSupportFragPresenterTag<*, *>
) : StatelessActionSupportFragPresenter {

    private var input: EditText? = null
    private var output: TextView? = null
    private var askButton: Button? = null
    private var pagerButton: Button? = null

    override fun onCreate(host: ActionSupportFragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: ActionSupportFragment, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View = host.UI {
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

            askButton = button {
                text = "Ask"
                setOnClickListener {
                    openDialog(host)
                }
            }

            pagerButton = button {
                text = "ViewPager sample"
                setOnClickListener {
                    openViewPagerSample(host)
                }
            }
        }
    }.view

    override fun onViewCreated(host: ActionSupportFragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
    }

    private fun openDialog(host: ActionSupportFragment) {
        openDialog(host,
                host.createDialogFragmentForResult(
                        questionPresenterTag, ParcelString(input!!.text.toString()),
                        OpenDialogRequestCode,
                        pureParcelFunction2(RootPresenter::gotResponse),
                        pureParcelFunction1(RootPresenter::onCancel)
                )
        )
    }

    private fun gotResponse(string: ParcelString) {
        output!!.text = string.value
    }

    private fun onCancel() {
        output!!.context.toast("Canceled.")
    }

    private fun openViewPagerSample(host: ActionSupportFragment) {
        openFragment(host, SupportFragment(pagerPresenterTag))
    }

    override fun onViewDestroyed(host: ActionSupportFragment) {
        input = null
        output = null
        askButton!!.setOnClickListener(null)
        askButton = null
        pagerButton!!.setOnClickListener(null)
        pagerButton = null
    }

    override fun onDestroy(host: ActionSupportFragment) {
    }

    private companion object {
        private const val OpenDialogRequestCode = 1
    }

}
