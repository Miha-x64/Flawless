package net.aquadc.flawless.sampleapp

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.implementMe.ActionV4FragPresenter
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.parcel.ParcelInt
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.tag.PresenterTag
import net.aquadc.flawless.tag.V4FragPresenterTag
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.tag.tag
import org.jetbrains.anko.matchParent

class PagerPresenter : ActionV4FragPresenter, PresenterFactory {

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
            override fun getItem(position: Int): Fragment = MvpV4Fragment(PagerItemPresenterTag, ParcelInt(position))
        }
    }

    override fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER = when (tag) {

        PagerItemPresenterTag -> PagerItemPresenter()

        else -> throw IllegalArgumentException()

    } as PRESENTER

    override fun onViewDestroyed(host: MvpV4Fragment<ParcelUnit>) {
    }

    override fun onDetach() {
    }

    private companion object {
        private val PagerItemPresenterTag
                by tag(of<PagerItemPresenter>())
    }

}
