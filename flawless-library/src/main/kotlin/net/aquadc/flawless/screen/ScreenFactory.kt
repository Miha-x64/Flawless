package net.aquadc.flawless.screen

import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.tag.AnyScreenTag

/**
 * A thing responsible for creating [Screen] instances.
 */
interface ScreenFactory {

    @Deprecated("use another overload", ReplaceWith("createScreen(request)"), DeprecationLevel.ERROR)
    fun createScreen(tag: AnyScreenTag, host: Host): Nothing = throw AssertionError()

    /**
     * Create a screen for the specified tag.
     * There's no language mechanism to make it type-safe,
     * so 'unchecked cast' happens, but heap pollution won't happen
     * if you're using `select { tag then { Screen(...) }`.
     */
    fun createScreen(intent: AnyScreenIntent): AnyScreen

}
