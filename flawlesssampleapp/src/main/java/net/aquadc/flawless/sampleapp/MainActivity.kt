package net.aquadc.flawless.sampleapp

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.tag.tag

class MainActivity : AppCompatActivity(), PresenterFactory {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, MvpV4Fragment(RootPresenterTag))
                    .commit()
        }
    }

    override fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER = when (tag) {

        RootPresenterTag -> RootPresenter(
                Companion::openFragment, Companion::openDialogFragment,
                DialogPresenterTag, PagerPresenterTag)

        DialogPresenterTag -> DialogPresenter()

        PagerPresenterTag -> PagerPresenter(PagerItemPresenterTag)

        PagerItemPresenterTag -> PagerItemPresenter()

        else -> throw UnsupportedOperationException()
    } as PRESENTER

    private companion object {

        private val RootPresenterTag
                by tag(of<RootPresenter>())

        private val DialogPresenterTag
                by tag(of<DialogPresenter>())

        private val PagerPresenterTag
                by tag(of<PagerPresenter>())

        private val PagerItemPresenterTag
                by tag(of<PagerItemPresenter>())

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
