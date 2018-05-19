package net.aquadc.flawless.sampleapp

import android.content.Context
import android.graphics.Color
import android.view.View
import net.aquadc.flawless.androidView.SupportBottomSheetDialogFragment
import net.aquadc.flawless.implementMe.StatelessSupportBottomSheetDialogFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


class BottomSheetDialogScreen : StatelessSupportBottomSheetDialogFragScreen<ParcelUnit, ParcelUnit> {

    override fun onCreate(host: SupportBottomSheetDialogFragment, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun createView(host: SupportBottomSheetDialogFragment, parent: Context, arg: ParcelUnit, state: ParcelUnit?): View =
            host.UI {
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

    override fun onViewCreated(host: SupportBottomSheetDialogFragment, view: View, arg: ParcelUnit, state: ParcelUnit?) {
    }

    override fun onViewDestroyed(host: SupportBottomSheetDialogFragment) {
    }

    override fun onDestroy(host: SupportBottomSheetDialogFragment) {
    }

}
