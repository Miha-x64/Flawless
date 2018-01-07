package net.aquadc.flawless.implementMe

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.parcel.ParcelUnit

interface StatelessPresenter<in ARG : Parcelable, RET : Parcelable, in HOST : Host<RET>, in PARENT, VIEW>
    : Presenter<ARG, RET, HOST, PARENT, VIEW, ParcelUnit> {
    override fun saveState(): ParcelUnit = ParcelUnit
}
