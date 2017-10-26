package net.aquadc.flawless.implementMe

import android.os.Parcelable

interface Presenter<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW, STATE : Parcelable> {

    /**
     * The presenter was attached to its host. It's time to, for example, set visibilityStateListener.
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
    fun onDetach()

}
