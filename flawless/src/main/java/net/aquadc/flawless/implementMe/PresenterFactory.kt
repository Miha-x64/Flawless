package net.aquadc.flawless.implementMe

import android.os.Parcelable

interface PresenterFactory {

    fun <ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> createPresenter(
            tag: String
    ): Presenter<ARG, RET, HOST, PARENT, VIEW>

}
