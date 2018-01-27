package net.aquadc.flawless.tag

import android.os.Parcel
import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.Presenter
import kotlin.reflect.KProperty

typealias AnyPresenterTag = PresenterTag<*, *, *, *, *, *>
class PresenterTag<in ARG : Parcelable, out RET : Parcelable, in HOST : Host, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW, *>>
internal constructor(
        private val thisRefStr: String,
        private val tag: String,
        private val presenterClassName: String,
        private val argClassName: String,
        private val retClassName: String
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(thisRefStr)
        dest.writeString(tag)
        dest.writeString(presenterClassName)
        dest.writeString(argClassName)
        dest.writeString(retClassName)
    }

    companion object CREATOR : Parcelable.Creator<
            PresenterTag<Parcelable, Parcelable, Host, Any?, Any?, Presenter<Parcelable, Parcelable, Host, Any?, Any?, *>>> {
        override fun createFromParcel(
                source: Parcel
        ): PresenterTag<Parcelable, Parcelable, Host, Any?, Any?, Presenter<Parcelable, Parcelable, Host, Any?, Any?, *>> =
                PresenterTag(
                        thisRefStr = source.readString(),
                        tag = source.readString(),
                        presenterClassName = source.readString(),
                        argClassName = source.readString(),
                        retClassName = source.readString()
                )
        override fun newArray(
                size: Int
        ): Array<PresenterTag<Parcelable, Parcelable, Host, Any?, Any?, Presenter<Parcelable, Parcelable, Host, Any?, Any?, *>>?> =
                arrayOfNulls(size)
    }

    operator fun getValue(thisRef: Any?, prop: KProperty<*>) =
            this

    override fun equals(other: Any?): Boolean =
            other is PresenterTag<*, *, *, *, *, *>
                    && other.thisRefStr == thisRefStr
                    && other.tag == tag

    override fun hashCode(): Int =
            thisRefStr.hashCode() xor tag.hashCode()

    override fun toString(): String =
            "tag $thisRefStr.$tag " +
                    "of $presenterClassName " +
                    "($argClassName) -> $retClassName"

}
