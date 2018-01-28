
This library is an attempt to allow use of composition in Android.

In Activity of parent Fragment, you implement ScreenFactory:

```kt
class MainActivity : AppCompatActivity(), ScreenFactory {

    ...

    override fun createScreen(tag: AnyScreenTag): AnyScreen = select(tag) {
        // composition: you can pass to constructor whatever you want
        RootScreenTag then { RootScreen(Companion::openDialogFragment, DialogScreenTag) }
        DialogScreenTag then ::DialogScreen
    }

    private companion object {

        private val RootScreenTag
                by tag(of<RootScreen>())

        private val DialogScreenTag
                by tag(of<DialogScreen>())
        //                ^ exact type of a screen

        fun openDialogFragment(host: Fragment, new: DialogFragment) {
            new.show(host.fragmentManager, null)
        }
    }

}
```

## Passing data

Library also helps you in passing arguments:

```kt
class RootScreen(
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionScreenTag: SupportDialogFragScreenTag<ParcelString, ParcelString, *>
) : StatelessSupportFragScreen<ParcelUnit, ParcelUnit> {
//                             ^ input     ^ output

    ...

    private fun openDialog() {
        openDialog(host,
                SupportDialogFragment(
                        questionScreenTag, // screen tag
                        ParcelString(input!!.text.toString()), // argument
                        host, OpenDialogRequestCode, // target, requestCode
                        pureParcelFunction2(RootScreen::gotResponse), // response callback
                        pureParcelFunction1(RootScreen::onCancel) // cancellation callback
                )
        )
    }

    private fun gotResponse(string: ParcelString) {
        output.text = string.value
    }

    ...

}
```

...and delivering results:
```kt
class DialogScreen : StatelessSupportDialogFragScreen<ParcelString, ParcelString> {

    ...

    override fun createView(host: SupportDialogFragment<ParcelString, ParcelString>, parent: Context, argument: ParcelString): Dialog {
        
        ...

        return AlertDialog.Builder(parent)
                .setTitle(argument.value)
                .setView(view)
                .setPositiveButton("Ok") { _, _ ->
                    returnValue = ParcelString(view.text.toString())
                }
                .setNegativeButton("Cancel", null)
                .create()
    }

    // will be automatically delivered when this fragment finish
    override var returnValue: ParcelString? = null
            private set

}
```

## Requesting permissions and starting activities

```kt
private fun takePhoto() {
    host.requestPermissions(
            RequestCameraPermCode,
            pureParcelFunction2(RootScreen::takePhotoPermResult),
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

    host.registerRawResultCallback(TakePhotoRequestCode, pureParcelFunction3(RootScreen::photoTaken))
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
