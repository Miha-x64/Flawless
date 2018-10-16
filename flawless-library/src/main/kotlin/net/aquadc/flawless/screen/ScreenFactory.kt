package net.aquadc.flawless.screen

/**
 * A thing responsible for creating [Screen] instances,
 * typically implemented by [android.app.Activity].
 */
interface ScreenFactory {

    /**
     * Create a screen for the specified tag.
     * There's no language mechanism to make it type-safe,
     * so 'unchecked cast' happens, but heap pollution won't happen
     * if you're using `select { tag then { Screen(...) }`.
     */
    fun createScreen(intent: AnyScreenIntent): AnyScreen

}
