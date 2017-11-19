package net.aquadc.flawless.sampleapp

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.AnyPresenter
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.tag.*

class MainActivity : AppCompatActivity(), PresenterFactory {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, SupportFragment(RootPresenterTag))
                    .commit()
        }
    }

    override fun createPresenter(tag: AnyPresenterTag): AnyPresenter = select(tag) {

        RootPresenterTag then {
            RootPresenter(
                    Companion::openFragment, Companion::openDialogFragment,
                    DialogPresenterTag, PagerPresenterTag, BottomSheetDialogPresenterTag)
        }

        DialogPresenterTag then ::DialogPresenter

        PagerPresenterTag then ::PagerPresenter

        BottomSheetDialogPresenterTag then ::BottomSheetDialogPresenter
    }

    private companion object {

        private val RootPresenterTag
                by tag(of<RootPresenter>())

        private val DialogPresenterTag
                by tag(of<DialogPresenter>())

        private val PagerPresenterTag
                by tag(of<PagerPresenter>())

        private val BottomSheetDialogPresenterTag
                by tag(of<BottomSheetDialogPresenter>())

        fun openFragment(host: Fragment, new: Fragment) {
            host.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, new)
                    .addToBackStack(null)
                    .commit()
        }

        fun openDialogFragment(host: Fragment, new: DialogFragment) {
            new.show(host.fragmentManager, null)
        }
    }

}
