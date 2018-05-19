package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.Screen

/**
 * `when`-like DSL for choosing screen.
 */
inline fun select(tag: AnyScreenTag, host: Host, thenCases: () -> Unit): AnyScreen {
    check(Select.currentTag == null)
    check(Select.matchingScreen == null)
    check(Select.currentHost == null)
    try {
        Select.currentTag = tag
        Select.currentHost = host

        thenCases()

        Select.matchingScreen?.let { return it }
        throw NoSuchElementException("Unable to find screen for the specified tag $tag")
    } finally {
        Select.currentTag = null
        Select.matchingScreen = null
        Select.currentHost = null
    }
}

/**
 * Acts like a specific `when` case — creates a screen if tag matches.
 */
inline infix fun <ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, SCR : Screen<ARG, RET, HOST, PARENT, VIEW, *>>
        ScreenTag<ARG, RET, HOST, PARENT, VIEW, SCR>.then(create: (host: HOST) -> SCR) {
    if (Select.matchingScreen != null) return
    check(Select.currentTag != null) { "called from wrong context" }
    if (this == Select.currentTag) Select.matchingScreen = @Suppress("UNCHECKED_CAST") create(Select.currentHost as HOST)
}

@PublishedApi
internal object Select {
    @PublishedApi internal var currentTag: AnyScreenTag? = null
    @PublishedApi internal var matchingScreen: AnyScreen? = null
    @PublishedApi internal var currentHost: Host? = null
}
