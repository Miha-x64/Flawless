package net.aquadc.flawless.screen

import android.os.Parcelable
import net.aquadc.flawless.androidView.Host

/**
 * Represents a platform-dependent UI implementation.
 * @param ARG type of supplied parameter
 * @param RET type of return value, delivered via, e. g., [android.support.v4.app.Fragment.onActivityResult]
 * @param HOST type of platform container
 * @param PARENT enclosing object, e. g. [android.view.View] or [android.content.Context]
 * @param VIEW type of view used by this [Screen], e. g. [android.view.View] or [android.app.Dialog]
 * @param STATE type of state-memento
 */
interface Screen<in ARG : Parcelable, out RET : Parcelable, in HOST : Host, in PARENT, VIEW, STATE : Parcelable> {


    /**
     * Host asks you to create a view.
     */
    fun createView(parent: PARENT): VIEW

    /**
     * The view has been just created, it's time to set listeners on UI controls.
     */
    fun viewAttached(view: VIEW)

    /**
     * The view has been destroyed.
     * It's high time to cancel pending operations, like network calls, and remove references to any View objects.
     */
    fun disposeView()

    /**
     * It's high time to save state.
     */
    fun saveState(): STATE

    /**
     * Host was destroyed. It's the end of current incarnation.
     */
    fun destroy()

    /**
     * A value which will be returned to target (to caller).
     * Will be requested only if current screen has a target
     * and will disappear (isRemoving/isFinishing).
     */
    val returnValue: RET?
        get() = null

}
