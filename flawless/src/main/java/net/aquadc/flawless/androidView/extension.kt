package net.aquadc.flawless.androidView

import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.PresenterFactory

internal fun Fragment.findPresenterFactory(): PresenterFactory {

    // bubble up through parents
    var parent = parentFragment
    while (parent != null) {
        if (parent is PresenterFactory)
            return parent

        parent = parent.parentFragment
    }

    val activity = activity
    if (activity is PresenterFactory)
        return activity

    throw NoSuchElementException("$this failed to find its parent implementing PresenterFactory. " +
            "You need to implement PresenterFactory in parent fragment or Activity.")

}
