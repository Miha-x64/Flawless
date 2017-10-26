package net.aquadc.flawless.implementMe

import android.os.Parcelable
import net.aquadc.flawless.tag.PresenterTag

interface PresenterFactory {

    fun <A : Parcelable, R : Parcelable, H, P, V, PRESENTER : Presenter<A, R, H, P, V, *>> createPresenter(
            tag: PresenterTag<A, R, H, P, V, PRESENTER>
    ): PRESENTER

}
