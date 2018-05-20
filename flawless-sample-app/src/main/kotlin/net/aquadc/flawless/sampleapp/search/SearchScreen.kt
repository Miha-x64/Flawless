package net.aquadc.flawless.sampleapp.search

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.screen.Screen
import net.aquadc.flawless.screen.SupportFragScreen
import net.aquadc.flawless.parcel.ParcelPair
import net.aquadc.flawless.parcel.ParcelString
import net.aquadc.flawless.screen.ScreenArgs
import net.aquadc.properties.MutableProperty
import net.aquadc.properties.android.bindings.bindTextBidirectionally
import org.jetbrains.anko.*


class SearchScreen<in ARG : Parcelable, out RET : Parcelable, STATE : Parcelable, SCR : Screen<ARG, RET, Host, ViewGroup?, View, STATE>>(
        req: ScreenArgs<ARG, out Host, ParcelPair<ParcelString, STATE>>,
        private val searchProp: MutableProperty<String>,
        private val nested: SCR
) : SupportFragScreen<ARG, RET, ParcelPair<ParcelString, STATE>> {

    init {
        req.state?.a?.value?.let { searchProp.value = it }
    }

    override fun createView(parent: ViewGroup): View = parent.context.UI {

        verticalLayout {
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)

            editText {
                hint = "Search"
                bindTextBidirectionally(searchProp)
            }.lparams(matchParent, wrapContent)

            frameLayout {
                addView(nested.createView(parent))
            }.lparams(matchParent, 0, 1f)
        }

    }.view

    override fun viewAttached(view: View) {
        nested.viewAttached(view)
    }

    override fun disposeView() {
        nested.disposeView()
    }

    override fun destroy() {
        nested.destroy()
    }

    override fun saveState(): ParcelPair<ParcelString, STATE> =
            ParcelPair(ParcelString(searchProp.value), nested.saveState())

    override val returnValue: RET?
        get() = nested.returnValue

}
