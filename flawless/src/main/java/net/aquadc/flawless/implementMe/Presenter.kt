package net.aquadc.flawless.implementMe

import android.os.Parcelable

interface Presenter<ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> {

    /**
     * The presenter was attached to its host. It's time to, for example, set visibilityStateListener.
     */
    fun onAttach(host: HOST, arg: ARG)

    /**
     * Hosts asks you to create a view.
     */
    fun createView(host: HOST, parent: PARENT, arg: ARG): VIEW

    /**
     * The view has been just created, it's time to set listeners on UI controls.
     */
    fun onViewCreated(host: HOST, view: VIEW, arg: ARG)

    /**
     * View has been destroyed.
     * It's high time to cancel pending operations, like network calls, and remove references to any View objects.
     */
    fun onViewDestroyed(host: HOST)

    /**
     * Host was destroyed. It's the end.
     */
    fun onDetach()

}
