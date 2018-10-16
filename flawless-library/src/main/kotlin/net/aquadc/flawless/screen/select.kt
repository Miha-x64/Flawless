package net.aquadc.flawless.screen

/**
 * `when`-like DSL for choosing screen.
 * `when` itself cannot be used in this case because it cannot un-erase type variables.
 * Even referential equality (`a === b`) won't map a's type parameters on b's ones.
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

// Why not top-level? Because, when we have a separate 'Select' class,
// 'SelectKt' class consisting of a single inline-function won't ever be loaded and verified.
@PublishedApi internal object Select {
    @[PublishedApi JvmField JvmSynthetic] internal var currentIntent: AnyScreenIntent? = null
    @[PublishedApi JvmField JvmSynthetic] internal var matchingScreen: AnyScreen? = null
}
