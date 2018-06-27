package net.aquadc.flawless.screen

import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.tag.AnyScreenTag

/**
 * `when`-like DSL for choosing screen.
 */
inline fun select(intent: AnyScreenIntent, thenCases: AnyScreenIntent.() -> Unit): AnyScreen {
    check(Select.currentIntent == null)
    check(Select.matchingScreen == null)
    try {
        Select.currentIntent = intent

        thenCases(intent)

        Select.matchingScreen?.let { return it }
        throw NoSuchElementException("Unable to find screen for the specified ${intent.tag}")
    } finally {
        Select.currentIntent = null
        Select.matchingScreen = null
    }
}

@Deprecated("use another overload", ReplaceWith("select(request)"), DeprecationLevel.ERROR)
fun select(tag: AnyScreenTag, host: Host, thenCases: () -> Unit): Nothing {
    throw AssertionError()
}

// Why not top-level? Because, when we have a separate 'Select' class,
// 'SelectKt' class consisting of a single inline-function won't ever be loaded and verified.
@PublishedApi internal object Select {
    @[PublishedApi JvmField JvmSynthetic] internal var currentIntent: AnyScreenIntent? = null
    @[PublishedApi JvmField JvmSynthetic] internal var matchingScreen: AnyScreen? = null
}
