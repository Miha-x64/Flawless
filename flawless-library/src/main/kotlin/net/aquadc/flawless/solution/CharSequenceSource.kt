package net.aquadc.flawless.solution

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import android.text.TextUtils
import net.aquadc.flawless.parcel.BoringParcelable


/**
 * A lazy [CharSequence] which may depend on current config, resources, etc or be a constant.
 */
interface CharSequenceSource : Parcelable {
    fun get(resources: Resources): CharSequence
}


/**
 * A [CharSequence] from `strings.xml` (or any other resource file).
 */
class CharSequenceFromResources(@param:StringRes private val res: Int) : BoringParcelable(), CharSequenceSource {
    override fun get(resources: Resources): CharSequence = resources.getText(res)
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = dest.writeInt(res)
    override fun equals(other: Any?): Boolean = other is CharSequenceFromResources && other.res == res
    override fun hashCode(): Int = res
    override fun toString(): String = "CharSequenceFromResources(#${res.toString(16)})"
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<CharSequenceFromResources> =
            object : Parcelable.Creator<CharSequenceFromResources> {
                override fun createFromParcel(source: Parcel): CharSequenceFromResources =
                    CharSequenceFromResources(source.readInt())
                override fun newArray(size: Int): Array<CharSequenceFromResources?> =
                    arrayOfNulls(size)
            }
    }
}

/**
 * A constant, hard-coded [CharSequence].
 */
class ConstantCharSequence(private val value: CharSequence) : BoringParcelable(), CharSequenceSource {
    override fun get(resources: Resources): CharSequence = value
    override fun writeToParcel(dest: Parcel, flags: Int): Unit = TextUtils.writeToParcel(value, dest, flags)
    override fun equals(other: Any?): Boolean = other is ConstantCharSequence && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "ConstantCharSequence($value)"
    private companion object {
        @JvmField val CREATOR: Parcelable.Creator<ConstantCharSequence> =
            object : Parcelable.Creator<ConstantCharSequence> {
                override fun createFromParcel(source: Parcel): ConstantCharSequence =
                    ConstantCharSequence(TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source))
                override fun newArray(size: Int): Array<ConstantCharSequence?> =
                    arrayOfNulls(size)
            }
    }
}


@JvmField val OkCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.ok)
@JvmField val CancelCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.cancel)

@Deprecated("Incorrectly matches android.R.string.ok rather than \"yes\".")
@JvmField val YesCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.yes)

@Deprecated("Incorrectly matches android.R.string.cancel rather than \"no\".")
@JvmField val NoCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.no)
