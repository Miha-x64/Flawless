package net.aquadc.flawless.sampleapp

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.extension.createDialogFragmentForResult
import net.aquadc.flawless.implementMe.StatelessActionV4FragPresenter
import net.aquadc.flawless.implementMe.V4FragPresenter
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.parcel.pureParcelFunction2
import net.aquadc.flawless.tag.SupplierV4FragPresenterTag
import net.aquadc.flawless.tag.V4DialogFragPresenterTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class RootPresenter(
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: V4DialogFragPresenterTag<ParcelString, ParcelString, *>,
        private val pagerPresenterTag: SupplierV4FragPresenterTag<*, *>
) : StatelessActionV4FragPresenter {

    private lateinit var host: MvpV4Fragment<ParcelUnit>
    private var input: EditText? = null
    private var output: TextView? = null
    private var askButton: Button? = null
    private var pagerButton: Button? = null

    override fun onCreate(host: MvpV4Fragment<ParcelUnit>, arg: ParcelUnit, state: ParcelUnit?) {
        this.host = host
    }

    override fun createView(host: MvpV4Fragment<ParcelUnit>, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View = host.UI {
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
                    openDialog()
                }
            }

            pagerButton = button {
                text = "ViewPager sample"
                setOnClickListener {
                    openViewPagerSample()
                }
            }
        }
    }.view

    override fun onViewCreated(host: MvpV4Fragment<ParcelUnit>, view: View, arg: ParcelUnit, state: ParcelUnit?) {
    }

    private fun openDialog() {
        openDialog(host,
                host.createDialogFragmentForResult(
                        questionPresenterTag, ParcelString(input!!.text.toString()), 1, pureParcelFunction2(RootPresenter::gotResponse)))
    }

    private fun gotResponse(string: ParcelString) {
        output!!.text = string.value
    }

    private fun openViewPagerSample() {
        openFragment(host, MvpV4Fragment(pagerPresenterTag))
    }

    override fun onViewDestroyed(host: MvpV4Fragment<ParcelUnit>) {
        input = null
        output = null
        askButton!!.setOnClickListener(null)
        askButton = null
        pagerButton!!.setOnClickListener(null)
        pagerButton = null
    }

    override fun onDestroy(host: MvpV4Fragment<ParcelUnit>) {
    }

}
