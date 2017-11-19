package net.aquadc.flawless.tag

import android.os.Parcelable
import net.aquadc.flawless.implementMe.Presenter

/**
 * `when`-like DSL for choosing presenter.
 */
inline fun select(tag: PresenterTag<*, *, *, *, *, *>, thenCases: () -> Unit): Presenter<*, *, *, *, *, *> {
    check(Select.currentTag == null)
    check(Select.matchingPresenter == null)
    try {
        Select.currentTag = tag
        thenCases()
        Select.matchingPresenter?.let { return it }
        throw NoSuchElementException("Unable to find presenter for the specified tag $tag")
    } finally {
        Select.currentTag = null
        Select.matchingPresenter = null
    }
}

/**
 * Acts like a specific `when` case — creates a presenter if tag matches.
 */
inline infix fun <ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW, PRESENTER : Presenter<ARG, RET, HOST, PARENT, VIEW, *>>
        PresenterTag<ARG, RET, HOST, PARENT, VIEW, PRESENTER>.then(create: () -> PRESENTER) {
    if (Select.matchingPresenter != null) return
    if (this == Select.currentTag) Select.matchingPresenter = create()
}

@PublishedApi
internal object Select {
    @PublishedApi internal var currentTag: PresenterTag<*, *, *, *, *, *>? = null
    @PublishedApi internal var matchingPresenter: Presenter<*, *, *, *, *, *>? = null
}
