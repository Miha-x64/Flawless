package net.aquadc.flawlesssampleapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.screen.*
import net.aquadc.flawless.tag.*
import org.jetbrains.anko.matchParent


class PagerScreen(
        private val req: StatelessActionScreenArgs<SupportFragment>
) : StatelessActionSupportFragScreen, ScreenFactory {

    override fun createView(parent: ViewGroup): View  =
            ViewPager(parent.context).apply {
                id = 1
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
            }

    override fun viewAttached(view: View) {
        (view as ViewPager).adapter = object : FragmentPagerAdapter(req.host.childFragmentManager) {
            override fun getCount(): Int = 5
            override fun getItem(position: Int): Fragment = SupportFragment(PagerItemScreenTag, ParcelInt(position))
        }
    }

    override fun createScreen(intent: AnyScreenIntent): AnyScreen = select(intent) {

        PagerItemScreenTag then { PagerItemScreen(args) }

    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

    private companion object {
        private val PagerItemScreenTag
                by tag(of<PagerItemScreen>())
    }

}
