package net.aquadc.flawless.tag

import android.os.Parcel
import android.os.Parcelable
import net.aquadc.flawless.implementMe.Presenter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty

class PresenterTag<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW>(
        private val tag: String,
        private val argClassHash: Int,
        private val retClassHash: Int,
        private val hostClassHash: Int,
        private val parentClassHash: Int,
        private val viewClassHash: Int
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(tag)
        dest.writeInt(argClassHash)
        dest.writeInt(retClassHash)
        dest.writeInt(hostClassHash)
        dest.writeInt(parentClassHash)
        dest.writeInt(viewClassHash)
    }

    companion object CREATOR : Parcelable.Creator<PresenterTag<Parcelable, Parcelable, Any, Any, Any>>{
        override fun createFromParcel(source: Parcel): PresenterTag<Parcelable, Parcelable, Any, Any, Any> =
                PresenterTag(
                        tag = source.readString(),
                        argClassHash = source.readInt(),
                        retClassHash = source.readInt(),
                        hostClassHash = source.readInt(),
                        parentClassHash = source.readInt(),
                        viewClassHash = source.readInt()
                )
        override fun newArray(size: Int): Array<PresenterTag<Parcelable, Parcelable, Any, Any, Any>?> =
                arrayOfNulls(size)
    }

    operator fun getValue(thisRef: Any, prop: KProperty<*>) =
            this

    override fun equals(other: Any?): Boolean =
            other is PresenterTag<*, *, *, *, *>
                    && other.tag == tag

    override fun hashCode(): Int =
            tag.hashCode()

    override fun toString(): String =
            "PresenterTag(" +
                    "$tag, " +
                    "arg#${argClassHash.hashCode().toString(16)}, " +
                    "ret#${retClassHash.hashCode().toString(16)}), " +
                    "host#${hostClassHash.hashCode().toString(16)}, " +
                    "parent#${parentClassHash.hashCode().toString(16)}, " +
                    "view#${viewClassHash.hashCode().toString(16)}" +
                    ")"

    fun checkPresenter(presenter: Presenter<*, *, *, *, *>) {
        val presenterInterface = presenter.javaClass.genericInterfaces.first {
            (it as? ParameterizedType)?.rawType == Presenter::class.java
        }
        if (presenterInterface !is ParameterizedType) {
            android.util.Log.e("PresenterTag", "Unexpected presenter type: $presenterInterface")
            return
        }

        val args = presenterInterface.actualTypeArguments
        checkArgHash("arg", args[0], argClassHash)
        checkArgHash("ret", args[1], retClassHash)
        checkArgHash("host", args[2], hostClassHash)
        checkArgHash("parent", args[3], parentClassHash)
        checkArgHash("view", args[4], viewClassHash)
    }

    private fun checkArgHash(name: String, type: Type, classHash: Int) {
        val klass = when (type) {
            is Class<*> -> type
            is ParameterizedType -> {
                android.util.Log.e("PresenterTag", "Presenter's $name type is $type, can't check its actual arguments correctness")
                type.rawType as Class<*>
            }
            else -> null
        }

        if (klass == null) {
            android.util.Log.e("PresenterTag", "Presenter's $name type is $type, don't know how to check it")
            return
        }

        check(klass.hashCode() == classHash) {
            "Wrong $name class: $type (#${type.hashCode()}). Expected: $classHash"
        }
    }

}
