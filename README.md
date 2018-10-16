
[ ![Download](https://api.bintray.com/packages/miha-x64/maven/net.aquadc.flawless%3Aflawless/images/download.svg) ](https://bintray.com/miha-x64/maven/net.aquadc.flawless%3Aflawless/_latestVersion)

This library is an attempt to allow use of composition in Android
(which is initially spoiled by no-arg constructors and reflection)
and add static typing to Fragments by using generics instead of `setArguments(Bundle)` and `onActivityResult(Intent)`.

```groovy
repositories {
    ...
    maven { url 'https://dl.bintray.com/miha-x64/maven' }
}

implementation 'net.aquadc.flawless:flawless:0.0.7'
```

In Activity of parent Fragment, you implement `ScreenFactory`:

```kt
class MainActivity : AppCompatActivity(), ScreenFactory {

    ...

    fun createScreen(intent: AnyScreenIntent): AnyScreen = select(intent) {
        // composition: you can pass to constructor whatever you want
        RootScreenTag then { RootScreen(args, Companion::openDialogFragment, DialogScreenTag) }
        DialogScreenTag then { DialogScreen(args) }
        // 'args: ScreenArgs' contains argument, host (Fragment, DialogFragment, etc), and saved state
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

```kt
class RootScreen(
        private val args: StatelessActionScreenArgs<SupportFragment>,
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionScreenTag: SupportDialogFragScreenTag<ParcelString, ParcelString, *>
) : StatelessSupportFragScreen<ParcelUnit, ParcelUnit> {
//                             ^ input     ^ output

    ...

    private fun openDialog() {
        val host = args.host
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

## Screen as a suspend-function

Screen consumes some arguments and returns a value after interacting with a user.
Thus it can be treated as a suspend-function.

Library does not provide any coroutine-based APIs at the moment because
continuations cannot be serialized yet:
[#76 in kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines/issues/76),
[#44 in kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization/issues/44).

You can check out a [sample flow](/flawless-sample-app/src/main/kotlin/net/aquadc/flawlesssampleapp/flow/FlowScreen.kt)
which should not be used in production because it cannot handle process death.
