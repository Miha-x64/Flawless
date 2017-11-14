package net.aquadc.flawless.sampleapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.ActionSupportFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.implementMe.StatelessActionSupportFragPresenter
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.tag.tag
import org.jetbrains.anko.matchParent

class PagerPresenter : StatelessActionSupportFragPresenter, PresenterFactory {

    override fun onCreate(host: ActionSupportFragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: ActionSupportFragment, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View =
            ViewPager(host.context).apply {
                id = 1
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
            }

    override fun onViewCreated(host: ActionSupportFragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
        (view as ViewPager).adapter = object : FragmentPagerAdapter(host.childFragmentManager) {
            override fun getCount(): Int = 5
            override fun getItem(position: Int): Fragment = SupportFragment(PagerItemPresenterTag, ParcelInt(position))
        }
    }

    override fun createPresenter(tag: PresenterTag<*, *, *, *, *, *>): Presenter<*, *, *, *, *, *> = when (tag) {

        PagerItemPresenterTag -> PagerItemPresenter()

        else -> throw IllegalArgumentException()

    }

    override fun onViewDestroyed(host: ActionSupportFragment) {
    }

    override fun onDestroy(host: ActionSupportFragment) {
    }

    private companion object {
        private val PagerItemPresenterTag
                by tag(of<PagerItemPresenter>())
    }

}
