package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.AnyScreen
import net.aquadc.flawless.implementMe.Screen

/**
 * `when`-like DSL for choosing screen.
 */
inline fun select(tag: AnyScreenTag, thenCases: () -> Unit): AnyScreen {
    check(Select.currentTag == null)
    check(Select.matchingScreen == null)
    try {
        Select.currentTag = tag
        thenCases()
        Select.matchingScreen?.let { return it }
        throw NoSuchElementException("Unable to find screen for the specified tag $tag")
    } finally {
        Select.currentTag = null
        Select.matchingScreen = null
    }
}

/**
 * Acts like a specific `when` case — creates a screen if tag matches.
 */
inline infix fun <ARG : Parcelable, RET : Parcelable, HOST : Host, PARENT, VIEW, SCR : Screen<ARG, RET, HOST, PARENT, VIEW, *>>
        ScreenTag<ARG, RET, HOST, PARENT, VIEW, SCR>.then(create: () -> SCR) {
    if (Select.matchingScreen != null) return
    if (this == Select.currentTag) Select.matchingScreen = create()
}

@PublishedApi
internal object Select {
    @PublishedApi internal var currentTag: ScreenTag<*, *, *, *, *, *>? = null
    @PublishedApi internal var matchingScreen: Screen<*, *, *, *, *, *>? = null
}
