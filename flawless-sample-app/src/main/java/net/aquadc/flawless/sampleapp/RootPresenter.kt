package net.aquadc.flawless.sampleapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.ActionSupportFragment
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.extension.createDialogFragmentForResult
import net.aquadc.flawless.implementMe.StatelessActionSupportFragPresenter
import net.aquadc.flawless.parcel.*
import net.aquadc.flawless.tag.SupplierSupportBottomSheetDialogFragPresenterTag
import net.aquadc.flawless.tag.SupplierSupportFragPresenterTag
import net.aquadc.flawless.tag.SupportDialogFragPresenterTag
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast

class RootPresenter(
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: SupportDialogFragPresenterTag<ParcelString, ParcelString, *>,
        private val pagerPresenterTag: SupplierSupportFragPresenterTag<*, *>,
        private val bottomSheetPresenterTag: SupplierSupportBottomSheetDialogFragPresenterTag<*, *>
) : StatelessActionSupportFragPresenter {

    private lateinit var host: ActionSupportFragment
    private var input: EditText? = null
    private var output: TextView? = null

    override fun onAttach(host: SupportFragment<ParcelUnit, ParcelUnit>) {
        this.host = host
    }

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

            button {
                text = "Ask"
                setOnClickListener {
                    openDialog(host)
                }
            }

            button {
                text = "ViewPager sample"
                setOnClickListener {
                    openViewPagerSample()
                }
            }

            button {
                text = "BottomSheet sample"
                setOnClickListener {
                    openBottomSheetSample()
                }
            }

            button {
                text = "Take a photo"
                setOnClickListener {
                    takePhoto()
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

    private fun openViewPagerSample() {
        openFragment(host, SupportFragment(pagerPresenterTag))
    }

    private fun openBottomSheetSample() {
        openDialog(host, SupportBottomSheetDialogFragment(bottomSheetPresenterTag))
    }

    private fun takePhoto() {
        if (ActivityCompat.checkSelfPermission(host.activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return host.toast("Camera permission was not granted.")
        }

        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (i.resolveActivity(host.activity.packageManager) == null) {
            return host.toast("Can't find app for taking pictures.")
        }

        host.registerRawResultCallback(TakePhotoRequestCode, pureParcelFunction3(RootPresenter::photoTaken))
        host.startActivityForResult(i, TakePhotoRequestCode)
    }

    private fun photoTaken(responseCode: Int, data: Intent?) {
        host.toast(when (responseCode) {
            Activity.RESULT_OK -> "OK"
            Activity.RESULT_CANCELED -> "Canceled"
            else -> "response code: $responseCode"
        })
    }

    override fun onViewDestroyed(host: ActionSupportFragment) {
        input = null
        output = null
    }

    override fun onDestroy(host: ActionSupportFragment) {
    }

    private companion object {
        private const val OpenDialogRequestCode = 1
        private const val TakePhotoRequestCode = 2
    }

}
