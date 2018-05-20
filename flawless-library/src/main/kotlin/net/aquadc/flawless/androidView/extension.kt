package net.aquadc.flawless.androidView

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.screen.AnyScreen
import net.aquadc.flawless.screen.Screen
import net.aquadc.flawless.screen.ScreenFactory
import net.aquadc.flawless.screen.ScreenArgs
import net.aquadc.flawless.screen.ScreenIntent
import net.aquadc.flawless.tag.ScreenTag


internal fun <F> F.createScreen(savedInstanceState: Bundle?): AnyScreen where F : Fragment, F : Host {
    val factory = findScreenFactory()
    val args = arguments
    val tag = args.getParcelable<ScreenTag<Parcelable, Parcelable, Host, Any?, Any?, Parcelable, Screen<Parcelable, Parcelable, Host, Any?, Any?, Parcelable>>>("tag")
    val arg = args.getParcelable<Parcelable>("arg")

    return factory.createScreen(ScreenIntent<
            Parcelable, Parcelable, Host, Any?, Any?, Parcelable, Screen<Parcelable, Parcelable, Host, Any?, Any?, Parcelable>
            >(tag, ScreenArgs(arg, this, savedInstanceState?.getParcelable("screen"))))
}

private fun Fragment.findScreenFactory(): ScreenFactory {

    // bubble up through parents
    var parent = parentFragment
    while (parent != null) {
        if (parent is ScreenFactory)
            return parent

        parent = parent.parentFragment
    }

    val activity = activity
    if (activity is ScreenFactory)
        return activity

    throw NoSuchElementException("$this failed to find its parent implementing ScreenFactory. " +
            "You need to implement ScreenFactory in parent Fragment or Activity.")

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

internal fun toString(toS: String, screen: Screen<*, *, *, *, *, *>?): String {
    val sb = StringBuilder(toS)
    sb.setLength(sb.length - 1)
    return sb.append(", screen=").append(screen).append('}').toString()
}

internal val Fragment.handler
    get() = activity?.window?.decorView?.handler ?: Handler()
