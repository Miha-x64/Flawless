package net.aquadc.flawless.sampleapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.aquadc.flawless.androidView.*
import net.aquadc.flawless.implementMe.StatelessActionSupportFragPresenter
import net.aquadc.flawless.parcel.*
import net.aquadc.flawless.tag.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast

class RootPresenter(
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: SupportDialogFragPresenterTag<ParcelString, ParcelString, *>,
        private val pagerPresenterTag: ActionSupportFragPresenterTag<*>,
        private val bottomSheetPresenterTag: ActionSupportBottomSheetDialogFragPresenterTag<*>
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
                SupportDialogFragment(
                        questionPresenterTag,
                        ParcelString(input!!.text.toString()),
                        host, OpenDialogRequestCode,
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
        host.requestPermissions(
                RequestCameraPermCode,
                pureParcelFunction2(RootPresenter::takePhotoPermResult),
                { _, userAgreed ->
                    AlertDialog.Builder(host.activity)
                            .setMessage("We need permission to camera to do this.")
                            .setPositiveButton("Let's grant", { _, _ -> userAgreed.run() })
                            .setNegativeButton("Meh", null)
                            .show()
                },
                Manifest.permission.CAMERA
        )
    }

    private fun takePhotoPermResult(granted: Collection<String>) {
        if (Manifest.permission.CAMERA !in granted) {
            return host.toast("Camera permission was denied.")
        }

        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhoto.resolveActivity(host.activity.packageManager) == null) {
            return host.toast("Can't find app for taking pictures.")
        }

        host.exchange.startActivity(
                takePhoto, TakePhotoRequestCode,
                pureParcelFunction3(RootPresenter::photoTaken)
        )
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
        private const val RequestCameraPermCode = 1
    }

}
