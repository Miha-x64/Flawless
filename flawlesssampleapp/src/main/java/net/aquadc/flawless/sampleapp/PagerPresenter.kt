package net.aquadc.flawless.sampleapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.ActionV4FragPresenter
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.V4FragPresenterTag
import org.jetbrains.anko.matchParent

class PagerPresenter(
        private val pagerItemTag: V4FragPresenterTag<ParcelInt, *, *>
) : ActionV4FragPresenter {

    override fun onAttach(host: MvpV4Fragment<ParcelUnit>, arg: ParcelUnit) {
    }

    override fun createView(host: MvpV4Fragment<ParcelUnit>, parent: ViewGroup?, arg: ParcelUnit): View =
            ViewPager(host.context).apply {
                id = 1
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
            }

    override fun onViewCreated(host: MvpV4Fragment<ParcelUnit>, view: View, arg: ParcelUnit) {
        (view as ViewPager).adapter = object : FragmentPagerAdapter(host.childFragmentManager) {
            override fun getCount(): Int = 5
            override fun getItem(position: Int): Fragment = MvpV4Fragment(pagerItemTag, ParcelInt(position))
        }
    }

    override fun onViewDestroyed(host: MvpV4Fragment<ParcelUnit>) {
    }

    override fun onDetach() {
    }

}
