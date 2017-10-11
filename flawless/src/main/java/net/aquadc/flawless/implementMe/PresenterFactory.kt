package net.aquadc.flawless.implementMe

import android.os.Parcelable
import net.aquadc.flawless.tag.PresenterTag

interface PresenterFactory {

    fun <ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> createPresenter(
            tag: PresenterTag<ARG, RET, HOST, PARENT, VIEW>
    ): Presenter<ARG, RET, HOST, PARENT, VIEW>

}
