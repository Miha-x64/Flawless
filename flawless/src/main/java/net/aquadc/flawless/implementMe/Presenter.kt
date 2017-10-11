package net.aquadc.flawless.implementMe

import android.os.Parcelable

interface Presenter<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> {

    fun createView(host: HOST, parent: PARENT, argument: ARG): VIEW
    fun detach()

}
