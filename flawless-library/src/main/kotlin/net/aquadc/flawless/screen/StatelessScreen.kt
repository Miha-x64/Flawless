package net.aquadc.flawless.screen

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.parcel.ParcelUnit

interface StatelessScreen<in ARG : Parcelable, out RET : Parcelable, in HOST : Host, in PARENT, VIEW>
    : Screen<ARG, RET, HOST, PARENT, VIEW, ParcelUnit> {

    override fun saveState(): ParcelUnit = ParcelUnit

}
