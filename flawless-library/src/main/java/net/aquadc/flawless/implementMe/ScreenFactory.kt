package net.aquadc.flawless.implementMe

import net.aquadc.flawless.tag.AnyScreenTag


interface ScreenFactory {

    /**
     * Create a screen for the specified tag.
     * There's no language mechanism to make it type-safe,
     * so 'unchecked cast' followed by runtime type-check.
     */
    fun createScreen(tag: AnyScreenTag): AnyScreen

    @Deprecated("", ReplaceWith("createScreen(tag)"), DeprecationLevel.ERROR)
    fun createPresenter(tag: AnyScreenTag): Nothing = error("")

}
