package net.aquadc.flawless.solution

import android.content.res.Resources
import android.support.annotation.StringRes

interface CharSequenceSource {
    fun get(resources: Resources): CharSequence
}

class CharSequenceFromResources(@param:StringRes private val res: Int) : CharSequenceSource {
    override fun get(resources: Resources): CharSequence = resources.getText(res)
}

class ConstantCharSequence(private val value: CharSequence) : CharSequenceSource {
    override fun get(resources: Resources): CharSequence = value
}


val OkCharSequence = CharSequenceFromResources(android.R.string.ok)
val CancelCharSequence = CharSequenceFromResources(android.R.string.cancel)
val YesCharSequence = CharSequenceFromResources(android.R.string.yes)
val NoCharSequence = CharSequenceFromResources(android.R.string.no)
