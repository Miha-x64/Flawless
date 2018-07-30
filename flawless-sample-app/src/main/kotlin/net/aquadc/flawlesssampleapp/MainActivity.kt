package net.aquadc.flawlesssampleapp

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.screen.AnyScreen
import net.aquadc.flawless.screen.ScreenFactory
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawlesssampleapp.flow.BillingScreen
import net.aquadc.flawlesssampleapp.flow.FlowScreen
import net.aquadc.flawlesssampleapp.flow.ShippingScreen
import net.aquadc.flawlesssampleapp.search.ListScreen
import net.aquadc.flawlesssampleapp.search.SearchScreen
import net.aquadc.flawlesssampleapp.search.StringHolder
import net.aquadc.flawless.screen.AnyScreenIntent
import net.aquadc.flawless.tag.of
import net.aquadc.flawless.screen.select
import net.aquadc.flawless.tag.tag
import net.aquadc.properties.map
import net.aquadc.properties.propertyOf


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

    override fun createScreen(intent: AnyScreenIntent): AnyScreen = select(intent) {

        RootScreenTag then {
            RootScreen(
                    args, Companion::openFragment, Companion::openDialogFragment,
                    DialogScreenTag, PagerScreenTag, BottomSheetDialogScreenTag, FlowScreenTag, SearchScreenTag
            )
        }

        DialogScreenTag then { DialogScreen(args) }

        PagerScreenTag then { PagerScreen(args) }

        BottomSheetDialogScreenTag then { BottomSheetDialogScreen() }

        FlowScreenTag then { FlowScreen(args, ShippingScreenTag, BillingScreenTag, Companion::openFragment) }
        ShippingScreenTag then { ShippingScreen(args, Companion::closeFragment) }
        BillingScreenTag then { BillingScreen(args, Companion::closeFragment) }

        SearchScreenTag then {
            val searchProp = propertyOf("")
            val listProp = searchProp.map { query ->
                val lq = query.toLowerCase()
                data.filter { lq in it.toLowerCase() }
            }
            SearchScreen(args, searchProp, ListScreen(listProp, ::StringHolder, StringHolder::bind))
        }
    }

    private companion object {

        val x by mapOf("x" to "y")

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
