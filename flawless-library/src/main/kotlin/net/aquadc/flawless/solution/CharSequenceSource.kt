package net.aquadc.flawless.solution

import android.content.res.Resources
import android.support.annotation.StringRes


/**
 * A lazy [CharSequence] which may depend on current config, resources, etc or be a constant.
 */
interface CharSequenceSource {
    fun get(resources: Resources): CharSequence
}


/**
 * A [CharSequence] from `strings.xml` (or any other resource file).
 */
class CharSequenceFromResources(@param:StringRes private val res: Int) : CharSequenceSource {
    override fun get(resources: Resources): CharSequence =
            resources.getText(res)

    override fun equals(other: Any?): Boolean =
            other is CharSequenceFromResources && other.res == res

    override fun hashCode(): Int =
            res

    override fun toString(): String =
            "CharSequenceFromResources(#${res.toString(16)})"
}

/**
 * A constant, hard-coded [CharSequence].
 */
class ConstantCharSequence(private val value: CharSequence) : CharSequenceSource {
    override fun get(resources: Resources): CharSequence =
            value

    override fun equals(other: Any?): Boolean =
            other is ConstantCharSequence && other.value == value

    override fun hashCode(): Int =
            value.hashCode()

    override fun toString(): String =
            "ConstantCharSequence($value)"
}


@JvmField val OkCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.ok)
@JvmField val CancelCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.cancel)
@JvmField val YesCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.yes)
@JvmField val NoCharSequence: CharSequenceSource = CharSequenceFromResources(android.R.string.no)
