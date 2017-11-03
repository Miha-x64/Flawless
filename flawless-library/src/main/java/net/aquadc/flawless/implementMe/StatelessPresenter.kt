package net.aquadc.flawless.implementMe

import android.os.Parcelable
import net.aquadc.flawless.parcel.ParcelUnit

interface StatelessPresenter<in ARG : Parcelable, out RET : Parcelable, HOST, PARENT, VIEW> : Presenter<ARG, RET, HOST, PARENT, VIEW, ParcelUnit> {
    override fun saveState(): ParcelUnit = ParcelUnit
}
