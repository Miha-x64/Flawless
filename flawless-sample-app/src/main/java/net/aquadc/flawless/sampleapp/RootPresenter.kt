package net.aquadc.flawless.sampleapp

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.ActionMvpV4Fragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.extension.createDialogFragmentForResult
import net.aquadc.flawless.implementMe.StatelessActionV4FragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.parcel.pureParcelFunction1
import net.aquadc.flawless.parcel.pureParcelFunction2
import net.aquadc.flawless.tag.SupplierV4FragPresenterTag
import net.aquadc.flawless.tag.SupportDialogFragPresenterTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class RootPresenter(
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: SupportDialogFragPresenterTag<ParcelString, ParcelString, *>,
        private val pagerPresenterTag: SupplierV4FragPresenterTag<*, *>
) : StatelessActionV4FragPresenter {

    private var input: EditText? = null
    private var output: TextView? = null
    private var askButton: Button? = null
    private var pagerButton: Button? = null

    override fun onCreate(host: ActionMvpV4Fragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: ActionMvpV4Fragment, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View = host.UI {
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

    override fun onViewCreated(host: ActionMvpV4Fragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
    }

    private fun openDialog(host: ActionMvpV4Fragment) {
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

    private fun openViewPagerSample(host: ActionMvpV4Fragment) {
        openFragment(host, MvpV4Fragment(pagerPresenterTag))
    }

    override fun onViewDestroyed(host: ActionMvpV4Fragment) {
        input = null
        output = null
        askButton!!.setOnClickListener(null)
        askButton = null
        pagerButton!!.setOnClickListener(null)
        pagerButton = null
    }

    override fun onDestroy(host: ActionMvpV4Fragment) {
    }

    private companion object {
        private const val OpenDialogRequestCode = 1
    }

}
