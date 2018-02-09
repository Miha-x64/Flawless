package net.aquadc.flawless.sampleapp.search

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.implementMe.SupportFragScreen
import net.aquadc.flawless.parcel.ParcelPair
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.properties.MutableProperty
import net.aquadc.properties.android.bindings.bindTextBidirectionally
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


class SearchScreen<ARG : Parcelable, RET : Parcelable, STATE : Parcelable, SCR : SupportFragScreen<ARG, RET, STATE>>(
        private val searchProp: MutableProperty<String>,
        private val nested: SupportFragScreen<ARG, RET, STATE>
) : SupportFragScreen<ARG, RET, ParcelPair<ParcelString, STATE>> {

    override fun onAttach(host: SupportFragment<ARG, RET>) {
        nested.onAttach(host)
    }

    override fun onCreate(host: SupportFragment<ARG, RET>, arg: ARG, state: ParcelPair<ParcelString, STATE>?) {
        nested.onCreate(host, arg, state?.b)
        state?.a?.value?.let { searchProp.value = it }
    }

    override fun createView(host: SupportFragment<ARG, RET>, parent: ViewGroup?, arg: ARG, state: ParcelPair<ParcelString, STATE>?): View = host.UI {

        verticalLayout {
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)

            editText {
                hint = "Search"
                bindTextBidirectionally(searchProp)
            }.lparams(matchParent, wrapContent)

            frameLayout {
                addView(nested.createView(host, parent, arg, state?.b))
            }.lparams(matchParent, 0, 1f)
        }

    }.view

    override fun onViewCreated(host: SupportFragment<ARG, RET>, view: View, arg: ARG, state: ParcelPair<ParcelString, STATE>?) {
        nested.onViewCreated(host, view, arg, state?.b)
    }

    override fun onViewDestroyed(host: SupportFragment<ARG, RET>) {
        nested.onViewDestroyed(host)
    }

    override fun onDestroy(host: SupportFragment<ARG, RET>) {
        nested.onDestroy(host)
    }

    override fun saveState(): ParcelPair<ParcelString, STATE> =
            ParcelPair(ParcelString(searchProp.value), nested.saveState())

    override val returnValue: RET?
        get() = nested.returnValue

}
