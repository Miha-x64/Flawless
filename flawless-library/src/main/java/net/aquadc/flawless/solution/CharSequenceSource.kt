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
