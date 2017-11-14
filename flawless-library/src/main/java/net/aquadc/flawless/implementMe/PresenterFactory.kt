package net.aquadc.flawless.implementMe

import net.aquadc.flawless.tag.PresenterTag

interface PresenterFactory {

    /**
     * Create a presenter for the specified tag.
     * There's no language mechanism to make it type-safe,
     * so 'unchecked cast' followed by runtime type-check.
     */
    fun createPresenter(tag: PresenterTag<*, *, *, *, *, *>): Presenter<*, *, *, *, *, *>

}
