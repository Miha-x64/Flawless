package net.aquadc.flawless.tag

import android.os.Parcel
import android.os.Parcelable
import kotlin.reflect.KProperty

class PresenterTag<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW>(
        private val tag: String
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(tag)
    }

    companion object CREATOR : Parcelable.Creator<PresenterTag<Parcelable, Parcelable, Any, Any, Any>>{
        override fun createFromParcel(source: Parcel): PresenterTag<Parcelable, Parcelable, Any, Any, Any> =
                PresenterTag(source.readString())
        override fun newArray(size: Int): Array<PresenterTag<Parcelable, Parcelable, Any, Any, Any>?> =
                arrayOfNulls(size)
    }

    operator fun getValue(thisRef: Any, prop: KProperty<*>) =
            this

}
