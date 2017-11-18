
This library is an attempt to allow use of composition in Android.

In Activity of parent Fragment, you implement PresenterFactory:

```kt
class MainActivity : AppCompatActivity(), PresenterFactory {

    ...

    override fun createPresenter(tag: PresenterTag<*, *, *, *, *, *>): Presenter<*, *, *, *, *, *> = when (tag) {
        // composition: you can pass to constructor whatever you want
        RootPresenterTag -> RootPresenter(Companion::openDialogFragment, DialogPresenterTag)
        DialogPresenterTag -> DialogPresenter()
        else -> throw UnsupportedOperationException(tag.toString())
    }

    private companion object {

        private val RootPresenterTag
                by tag(of<RootPresenter>())

        private val DialogPresenterTag
                by tag(of<DialogPresenter>())
        //                ^ exact type of a presenter
        //      Important: library performs runtime type checking.

        fun openDialogFragment(host: Fragment, new: DialogFragment) {
            new.show(host.fragmentManager, null)
        }
    }

}
```

## Passing data

Library also helps you in passing arguments:

```kt
class RootPresenter(
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: V4DialogFragPresenterTag<ParcelString, ParcelString, *>
) : StatelessSupportFragPresenter<ParcelUnit, ParcelUnit> {
//                                ^ input     ^ output

    ...

    private fun openDialog() {
        openDialog(host,
                host.createDialogFragmentForResult(
                        questionPresenterTag, ParcelString(input!!.text.toString()), 1, pureParcelFunction2(RootPresenter::gotResponse)))
        //              ^ presenter tag       ^ argument                request code ^                      ^ result handler
    }

    private fun gotResponse(string: ParcelString) {
        output.text = string.value
    }

    ...

}
```

...and delivering results:
```kt
class DialogPresenter : StatelessSupportDialogFragPresenter<ParcelString, ParcelString> {

    ...

    override fun createView(host: SupportDialogFragment<ParcelString, ParcelString>, parent: Context, argument: ParcelString): Dialog {
        
        ...

        return AlertDialog.Builder(parent)
                .setTitle(argument.value)
                .setView(view)
                .setPositiveButton("Ok", { _, _ ->
                    host.deliverResult(ParcelString(view.text.toString()))
                    delivered = true
                })
                .setNegativeButton("Cancel", null)
                .create()
    }

    override fun onDestroy(host: SupportDialogFragment<ParcelString, ParcelString>) {
        if (!delivered) {
            host.deliverCancellation() // Warning: must do it!
        }
    }

}
```

## Requesting permissions and starting activities

```kt
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
```