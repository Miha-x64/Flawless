
This library is an attempt to allow use of composition in Android.

In Activity of parent Fragment, you implement PresenterFactory:

```kt
class MainActivity : AppCompatActivity(), PresenterFactory {

    ...

    override fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER = when (tag) {
        // composition: you can pass to constructor whatever you want
        RootPresenterTag -> RootPresenter(Companion::openDialogFragment, DialogPresenterTag)
        DialogPresenterTag -> DialogPresenter()
        else -> throw UnsupportedOperationException()
    } as PRESENTER

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

Library also helps you in passing arguments:

```kt
class RootPresenter(
        private val openDialog: (Fragment, DialogFragment) -> Unit,
        private val questionPresenterTag: V4DialogFragPresenterTag<ParcelString, ParcelString, *>
) : V4FragPresenter<ParcelUnit, ParcelUnit> {
//                  ^ input     ^ output

    ...

    private fun openDialog() {
        val dialog =
                MvpDialogFragmentV4(questionPresenterTag, ParcelString(input.text.toString()))
                //                  ^ presenter tag       ^ argument

        host.willStartForResult(dialog, 1, pureParcelFunction2(RootPresenter::gotResponse))
        //                                                     ^ result handler
        openDialog(host, dialog)
    }

    private fun gotResponse(string: ParcelString) {
        output.text = string.value
    }

    ...

}
```

...and delivering results:
```kt
class DialogPresenter : V4DialogFragPresenter<ParcelString, ParcelString> {

    ...

    override fun createView(host: MvpDialogFragmentV4<ParcelString>, parent: Context, argument: ParcelString): Dialog {
        
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

    override fun onDetach() {
        if (!delivered) {
            host.deliverCancellation() // Warning: must do it!
        }
    }

}
```

This is extremely experimental, because unchecked casts are everywhere.
