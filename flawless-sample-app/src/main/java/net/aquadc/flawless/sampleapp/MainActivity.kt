package net.aquadc.flawless.sampleapp

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import net.aquadc.flawless.ProxyHost
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.ScreenFactory
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.sampleapp.flow.BillingScreen
import net.aquadc.flawless.sampleapp.flow.FlowScreen
import net.aquadc.flawless.sampleapp.flow.ShippingScreen
import net.aquadc.flawless.sampleapp.search.ListScreen
import net.aquadc.flawless.sampleapp.search.SearchScreen
import net.aquadc.flawless.sampleapp.search.StringHolder
import net.aquadc.flawless.tag.*
import net.aquadc.properties.map
import net.aquadc.properties.propertyOf
import net.aquadc.properties.unsynchronizedMutablePropertyOf


class MainActivity : AppCompatActivity(), ScreenFactory {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, SupportFragment(RootScreenTag))
                    .commit()
        }
    }

    override fun createScreen(tag: AnyScreenTag, host: Host): AnyScreen = select(tag, host) {

        RootScreenTag then {
            RootScreen(
                    it, Companion::openFragment, Companion::openDialogFragment,
                    DialogScreenTag, PagerScreenTag, BottomSheetDialogScreenTag, FlowScreenTag, SearchScreenTag
            )
        }

        DialogScreenTag then { DialogScreen() }

        PagerScreenTag then { PagerScreen() }

        BottomSheetDialogScreenTag then { BottomSheetDialogScreen() }

        FlowScreenTag then { FlowScreen(ShippingScreenTag, BillingScreenTag, Companion::openFragment) }
        ShippingScreenTag then { ShippingScreen(Companion::closeFragment) }
        BillingScreenTag then { BillingScreen(Companion::closeFragment) }

        SearchScreenTag then {
            val searchProp = propertyOf("")
            val listProp = searchProp.map { query ->
                val lq = query.toLowerCase()
                data.filter { lq in it.toLowerCase() }
            }
            SearchScreen(ProxyHost(it), searchProp, ListScreen(listProp, ::StringHolder, StringHolder::bind))
        }
    }

    private companion object {

        val RootScreenTag
                by tag(of<RootScreen>())

        val DialogScreenTag
                by tag(of<DialogScreen>())

        val PagerScreenTag
                by tag(of<PagerScreen>())

        val BottomSheetDialogScreenTag
                by tag(of<BottomSheetDialogScreen>())

        val FlowScreenTag
                by tag(of<FlowScreen>())
        val ShippingScreenTag
                by tag(of<ShippingScreen>())
        val BillingScreenTag
                by tag(of<BillingScreen>())

        val SearchScreenTag
                by tag(of<SearchScreen<ParcelUnit, ParcelUnit, ParcelUnit, ListScreen<String, StringHolder>>>())

        fun openFragment(host: Fragment, new: Fragment) {
            host.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, new)
                    .addToBackStack(null)
                    .commit()
        }

        fun closeFragment(host: Fragment) {
            host.fragmentManager.popBackStackImmediate()
        }

        fun openDialogFragment(host: Fragment, new: DialogFragment) {
            new.show(host.fragmentManager, null)
        }

        val data = arrayOf("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven")
    }

}
