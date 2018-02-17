package net.aquadc.flawless.sampleapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.ScreenFactory
import net.aquadc.flawless.implementMe.StatelessActionSupportFragScreen
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.AnyScreenTag
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.tag.tag
import org.jetbrains.anko.matchParent


class PagerScreen : StatelessActionSupportFragScreen, ScreenFactory {

    override fun onCreate(host: SupportFragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: SupportFragment, parent: ViewGroup, arg: ParcelUnit, state: ParcelUnit?): View =
            ViewPager(host.context).apply {
                id = 1
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
            }

    override fun onViewCreated(host: SupportFragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
        (view as ViewPager).adapter = object : FragmentPagerAdapter(host.childFragmentManager) {
            override fun getCount(): Int = 5
            override fun getItem(position: Int): Fragment = SupportFragment(PagerItemScreenTag, ParcelInt(position))
        }
    }

    override fun createScreen(tag: AnyScreenTag): AnyScreen = when (tag) {

        PagerItemScreenTag -> PagerItemScreen()

        else -> throw IllegalArgumentException()

    }

    override fun onViewDestroyed(host: SupportFragment) {
    }

    override fun onDestroy(host: SupportFragment) {
    }

    private companion object {
        private val PagerItemScreenTag
                by tag(of<PagerItemScreen>())
    }

}
