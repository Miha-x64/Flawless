package net.aquadc.flawless.implementMe

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host

interface Presenter<in ARG : Parcelable, in RET : Parcelable, in HOST : Host<RET>, in PARENT, VIEW, STATE : Parcelable> {

    /**
     * The presenter was attached to its host.
     * This should happen before [PresenterFactory.createPresenter], if you implement [PresenterFactory].
     */
    fun onAttach(host: HOST) = Unit

    /**
     * The host was created. It's time to, for example, set visibilityStateListener.
     */
    fun onCreate(host: HOST, arg: ARG, state: STATE?)

    /**
     * Hosts asks you to create a view.
     */
    fun createView(host: HOST, parent: PARENT, arg: ARG, state: STATE?): VIEW

    /**
     * The view has been just created, it's time to set listeners on UI controls.
     */
    fun onViewCreated(host: HOST, view: VIEW, arg: ARG, state: STATE?)

    /**
     * View has been destroyed.
     * It's high time to cancel pending operations, like network calls, and remove references to any View objects.
     */
    fun onViewDestroyed(host: HOST)

    /**
     * High time to save state.
     */
    fun saveState(): STATE

    /**
     * Host was destroyed. It's the end of current incarnation.
     */
    fun onDestroy(host: HOST)

}
