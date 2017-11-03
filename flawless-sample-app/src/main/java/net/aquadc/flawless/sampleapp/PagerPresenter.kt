package net.aquadc.flawless.sampleapp

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.ActionMvpV4Fragment
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.StatelessActionV4FragPresenter
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.tag.tag
import org.jetbrains.anko.matchParent

class PagerPresenter : StatelessActionV4FragPresenter, PresenterFactory {

    override fun onCreate(host: ActionMvpV4Fragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: ActionMvpV4Fragment, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View =
            ViewPager(host.context).apply {
                id = 1
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
            }

    override fun onViewCreated(host: ActionMvpV4Fragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
        (view as ViewPager).adapter = object : FragmentPagerAdapter(host.childFragmentManager) {
            override fun getCount(): Int = 5
            override fun getItem(position: Int): Fragment = MvpV4Fragment(PagerItemPresenterTag, ParcelInt(position))
        }
    }

    override fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V, *>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER = when (tag) {

        PagerItemPresenterTag -> PagerItemPresenter()

        else -> throw IllegalArgumentException()

    } as PRESENTER

    override fun onViewDestroyed(host: ActionMvpV4Fragment) {
    }

    override fun onDestroy(host: ActionMvpV4Fragment) {
    }

    private companion object {
        private val PagerItemPresenterTag
                by tag(of<PagerItemPresenter>())
    }

}
