package net.aquadc.flawless.implementMe

import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.tag.AnyScreenTag


interface ScreenFactory {

    /**
     * Create a screen for the specified tag.
     * There's no language mechanism to make it type-safe,
     * so 'unchecked cast' happens, but heap pollution won't happen
     * if you're using `select { tag then { Screen(...) }`.
     */
    fun createScreen(tag: AnyScreenTag, host: Host): AnyScreen

}
