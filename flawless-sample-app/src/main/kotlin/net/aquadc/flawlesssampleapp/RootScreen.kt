package net.aquadc.flawlesssampleapp

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
import net.aquadc.flawless.screen.StatelessActionSupportFragScreen
import net.aquadc.flawless.parcel.*
import net.aquadc.flawless.screen.StatelessActionScreenArgs
import net.aquadc.flawless.tag.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast


class RootScreen constructor(
        private val args: StatelessActionScreenArgs<SupportFragment>,
        private val openFragment: (Fragment, Fragment) -> Unit,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionScreenTag: SupportDialogFragScreenTag<ParcelString, ParcelString, *>,
        private val pagerScreenTag: ActionSupportFragScreenTag<*>,
        private val bottomSheetScreenTag: ActionSupportBottomSheetDialogFragScreenTag<*>,
        private val flowTag: ActionSupportFragScreenTag<*>,
        private val searchTag: ActionSupportFragScreenTag<*>
) : StatelessActionSupportFragScreen {
    private var input: EditText? = null
    private var output: TextView? = null

    private inline val host: SupportFragment
        get() = args.host

    override fun createView(parent: ViewGroup): View = host.UI {
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

            button {
                text = "Start a flow"
                setOnClickListener {
                    startFlow()
                }
            }

            button {
                text = "Show search screen (decorator)"
                setOnClickListener {
                    search()
                }
            }
        }
    }.view


    override fun viewAttached(view: View) {
    }

    private fun openDialog(host: SupportFragment) {
        openDialog(host,
                SupportDialogFragment(
                        questionScreenTag,
                        ParcelString(input!!.text.toString()),
                        host, OpenDialogRequestCode,
                        pureParcelFunction(RootScreen::gotResponse),
                        pureParcelFunction(RootScreen::onCancel)
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
        openFragment(host, SupportFragment(pagerScreenTag))
    }

    private fun openBottomSheetSample() {
        openDialog(host, SupportBottomSheetDialogFragment(bottomSheetScreenTag))
    }

    private fun takePhoto() {
        host.requestPermissions(
                this,
                RequestCameraPermCode,
            pureParcelFunction(RootScreen::takePhotoPermResult),
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
            host.toast("Camera permission was denied.")
            return
        }

        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhoto.resolveActivity(host.activity.packageManager) == null) {
            host.toast("Can't find app for taking pictures.")
            return
        }

        host.exchange.startActivity(
                this, takePhoto, TakePhotoRequestCode,
            pureParcelFunction(RootScreen::photoTaken)
        )
    }

    private fun photoTaken(responseCode: Int, data: Intent?) {
        host.toast(when (responseCode) {
            Activity.RESULT_OK -> "OK"
            Activity.RESULT_CANCELED -> "Canceled"
            else -> "response code: $responseCode"
        })
    }

    private fun startFlow() {
        openFragment(host, SupportFragment(flowTag))
    }

    private fun search() {
        openFragment(host, SupportFragment(searchTag))
    }

    override fun disposeView() {
        input = null
        output = null
    }

    override fun destroy() {
    }

    private companion object {
        private const val OpenDialogRequestCode = 1
        private const val TakePhotoRequestCode = 2
        private const val RequestCameraPermCode = 1
    }

}
