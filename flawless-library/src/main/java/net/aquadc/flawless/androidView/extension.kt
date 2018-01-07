package net.aquadc.flawless.androidView

import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Presenter
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

// borrowed from
// https://github.com/Arello-Mobile/Moxy/blob/develop/moxy-app-compat/src/main/java/com/arellomobile/mvp/MvpAppCompatFragment.java#L71
internal fun Fragment.isFinishing(isStateSaved: Boolean): Boolean {
    val activity = activity
            ?: return true

    //We leave the screen and respectively all fragments will be destroyed
    if (activity.isFinishing)
        return true

    // When we rotate device isRemoving() return true for fragment placed in backstack
    // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
    if (isStateSaved)
        return false

    if (isRemoving)
        return true

    // See https://github.com/Arello-Mobile/Moxy/issues/24
    var parent = parentFragment
    while (parent != null) {
        if (parent.isRemoving)
            return true
        parent = parent.parentFragment
    }

    return false
}

internal fun toString(toS: String, presenter: Presenter<*, *, *, *, *, *>?): String {
    val sb = StringBuilder(toS)
    sb.setLength(sb.length - 1)
    return sb.append(", presenter=").append(presenter).append('}').toString()
}
