package net.aquadc.flawless.sampleapp.search

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.ProxyHost
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.Screen
import net.aquadc.flawless.implementMe.SupportFragScreen
import net.aquadc.flawless.parcel.ParcelPair
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.properties.MutableProperty
import net.aquadc.properties.android.bindings.bindTextBidirectionally
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


class SearchScreen<in ARG : Parcelable, out RET : Parcelable, STATE : Parcelable, SCR : Screen<ARG, RET, Host, ViewGroup?, View, STATE>>(
        private val searchProp: MutableProperty<String>,
        private val nested: SCR
) : SupportFragScreen<ARG, RET, ParcelPair<ParcelString, STATE>> {

    private lateinit var nestedHost: Host

    override fun onAttach(host: SupportFragment) {
        nestedHost = ProxyHost(host)
        nested.onAttach(nestedHost)
    }

    override fun onCreate(host: SupportFragment, arg: ARG, state: ParcelPair<ParcelString, STATE>?) {
        nested.onCreate(nestedHost, arg, state?.b)
        state?.a?.value?.let { searchProp.value = it }
    }

    override fun createView(host: SupportFragment, parent: ViewGroup, arg: ARG, state: ParcelPair<ParcelString, STATE>?): View = host.UI {

        verticalLayout {
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)

            editText {
                hint = "Search"
                bindTextBidirectionally(searchProp)
            }.lparams(matchParent, wrapContent)

            frameLayout {
                addView(nested.createView(nestedHost, parent, arg, state?.b))
            }.lparams(matchParent, 0, 1f)
        }

    }.view

    override fun onViewCreated(host: SupportFragment, view: View, arg: ARG, state: ParcelPair<ParcelString, STATE>?) {
        nested.onViewCreated(nestedHost, view, arg, state?.b)
    }

    override fun onViewDestroyed(host: SupportFragment) {
        nested.onViewDestroyed(nestedHost)
    }

    override fun onDestroy(host: SupportFragment) {
        nested.onDestroy(nestedHost)
    }

    override fun saveState(): ParcelPair<ParcelString, STATE> =
            ParcelPair(ParcelString(searchProp.value), nested.saveState())

    override val returnValue: RET?
        get() = nested.returnValue

}
