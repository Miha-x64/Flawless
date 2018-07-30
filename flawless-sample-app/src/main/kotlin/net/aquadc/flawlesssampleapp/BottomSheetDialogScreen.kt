package net.aquadc.flawlesssampleapp

import android.content.Context
import android.graphics.Color
import android.view.View
import net.aquadc.flawless.screen.StatelessSupportBottomSheetDialogFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.*


class BottomSheetDialogScreen : StatelessSupportBottomSheetDialogFragScreen<ParcelUnit, ParcelUnit> {

    override fun createView(parent: Context): View =
            parent.UI {
                verticalLayout {
                    padding = dip(16)

                    textView {
                        text = "Bottom sh**t"
                        textSize = 20f
                        textColor = Color.BLACK
                    }

                    textView {
                        text = "I don't know what to put here,\nso this is just another TextView"
                        textSize = 16f
                        textColor = Color.DKGRAY
                    }.lparams(width = matchParent) { topMargin = dip(8) }

                }
            }.view

    override fun viewAttached(view: View) {
    }

    override fun disposeView() {
    }

    override fun destroy() {
    }

}
