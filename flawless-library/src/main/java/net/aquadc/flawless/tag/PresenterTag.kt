package net.aquadc.flawless.tag

import android.os.Parcel
import android.os.Parcelable
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.implementMe.StatelessPresenter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty

class PresenterTag<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW, *>>(
        private val tag: String,
        private val presenterClassName: String,
        private val argClassName: String,
        private val retClassName: String,
        private val hostClassName: String,
        private val parentClassName: String,
        private val viewClassName: String
) : Parcelable {

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(tag)
        dest.writeString(presenterClassName)
        dest.writeString(argClassName)
        dest.writeString(retClassName)
        dest.writeString(hostClassName)
        dest.writeString(parentClassName)
        dest.writeString(viewClassName)
    }

    companion object CREATOR : Parcelable.Creator<
            PresenterTag<Parcelable, Parcelable, Any?, Any?, Any?, Presenter<Parcelable, Parcelable, Any?, Any?, Any?, *>>> {
        override fun createFromParcel(
                source: Parcel
        ): PresenterTag<Parcelable, Parcelable, Any?, Any?, Any?, Presenter<Parcelable, Parcelable, Any?, Any?, Any?, *>> =
                PresenterTag(
                        tag = source.readString(),
                        presenterClassName = source.readString(),
                        argClassName = source.readString(),
                        retClassName = source.readString(),
                        hostClassName = source.readString(),
                        parentClassName = source.readString(),
                        viewClassName = source.readString()
                )
        override fun newArray(
                size: Int
        ): Array<PresenterTag<Parcelable, Parcelable, Any?, Any?, Any?, Presenter<Parcelable, Parcelable, Any?, Any?, Any?, *>>?> =
                arrayOfNulls(size)
    }

    operator fun getValue(thisRef: Any, prop: KProperty<*>) =
            this

    override fun equals(other: Any?): Boolean =
            other is PresenterTag<*, *, *, *, *, *>
                    && other.tag == tag

    override fun hashCode(): Int =
            tag.hashCode()

    override fun toString(): String =
            "PresenterTag(" +
                    "$tag, " +
                    "arg($argClassName), " +
                    "ret($retClassName), " +
                    "host($hostClassName), " +
                    "parent($parentClassName), " +
                    "view($viewClassName), " +
                    "presenter($presenterClassName)" +
                    ")"

    fun checkPresenter(presenter: Presenter<*, *, *, *, *, *>) {
        // Sadness. Resolving actual type arguments is really complicated. I may use a library...
        var supertype: Type = presenter.javaClass
        loop@ while (true) {
            when (supertype) {
               is Class<*> -> {
                    if (supertype.superclass != null && Presenter::class.java.isAssignableFrom(supertype.superclass)) {
                        supertype = supertype.genericSuperclass
                    } else {
                        val presenterIface = supertype.genericInterfaces.single {
                            it is ParameterizedType && Presenter::class.java.isAssignableFrom(it.rawType as Class<*>) ||
                                    it is Class<*> && Presenter::class.java.isAssignableFrom(it)
                        }
                        supertype = presenterIface
                        if (supertype == Presenter::class.java || supertype == StatelessPresenter::class.java) break@loop
                    }
                }
                is ParameterizedType -> {
                    if (supertype.rawType.let { it == Presenter::class.java || it == StatelessPresenter::class.java }) break@loop
                    else supertype = supertype.rawType
                }
                else -> throw IllegalArgumentException("Unexpected supertype: $supertype of ${presenter.javaClass}")
            }
        }
        val presenterInterface = supertype as? ParameterizedType ?: run {
            android.util.Log.d("PresenterTag", "Unexpected supertype: $supertype of ${presenter.javaClass}")
            return
        }

        val args = presenterInterface.actualTypeArguments
        checkArgType("presenter", presenter.javaClass, presenterClassName)
        checkArgType("arg", args[0], argClassName)
        checkArgType("ret", args[1], retClassName)
        checkArgType("host", args[2], hostClassName)
        checkArgType("parent", args[3], parentClassName)
        checkArgType("view", args[4], viewClassName)
    }

    private fun checkArgType(name: String, type: Type, className: String) {
        val klass = when (type) {
            is Class<*> -> type
            is ParameterizedType -> {
                android.util.Log.d("PresenterTag",
                        "Presenter's $name type is $type, can't check its actual arguments correctness")
                type.rawType as Class<*>
            }
            else -> null
        }

        if (klass == null) {
            android.util.Log.d("PresenterTag", "Presenter's $name type is $type, don't know how to check it")
            return
        }

        check(klass.name == className) {
            "Wrong $name class: $type. Expected: $className"
        }
    }

}
