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

    /*
    IDEA can help migrating method calls, but not method overrides.
    Comments contain 'search' regular expressions and 'replace' expressions
    so you can try semi-automatic migration.
     */

    @Deprecated("just use constructor", ReplaceWith("init"), DeprecationLevel.ERROR)
    fun onCreate(host: HOST, arg: ARG, state: STATE?): Unit = throw AssertionError()
    // override fun onCreate\(host: \w+, arg: \w+, state\: \w+\?\) \{\s+\}
    // init



    @Deprecated("everything can now be passed to the constructor", ReplaceWith("createView(parent)"), DeprecationLevel.ERROR)
    fun createView(host: HOST, parent: PARENT, arg: ARG, state: STATE?): VIEW = throw AssertionError()
    // override fun createView\(host: \w+, parent: (\w+), arg: \w+, state: \w+\?\): (\w+)

    /**
     * Hosts asks you to create a view.
     */
    fun createView(parent: PARENT): VIEW
    // override fun createView(parent: $1): $2



    @Deprecated("renamed; everything can now be passed to the constructor", ReplaceWith("viewAttached(view)"), DeprecationLevel.ERROR)
    fun onViewCreated(host: HOST, view: VIEW, arg: ARG, state: STATE?): Unit = throw AssertionError()
    // override fun onViewCreated\(host: \w+, view: (\w+), arg: \w+, state: \w+\?\)

    /**
     * The view has been just created, it's time to set listeners on UI controls.
     */
    fun viewAttached(view: VIEW)
    // override fun viewAttached(view: $1)



    @Deprecated("renamed; everything can now be passed to the constructor", ReplaceWith("disposeView()"), DeprecationLevel.ERROR)
    fun onViewDestroyed(host: HOST): Unit = throw AssertionError()
    // override fun onViewDestroyed\(host: \w+\)

    /**
     * View has been destroyed.
     * It's high time to cancel pending operations, like network calls, and remove references to any View objects.
     */
    fun disposeView()
//    override fun disposeView()



    /**
     * High time to save state.
     */
    fun saveState(): STATE



    @Deprecated("everything can now be passed to the constructor", ReplaceWith("destroy()"), DeprecationLevel.ERROR)
    fun onDestroy(host: HOST): Unit = throw AssertionError()
    // override fun onDestroy\(host: \w+\)

    /**
     * Host was destroyed. It's the end of current incarnation.
     */
    fun destroy()
    // override fun destroy()



    /**
     * A value which will be returned to target (to caller).
     * Will be requested only if current screen has a target
     * and will disappear (isRemoving/isFinishing).
     */
    val returnValue: RET?
        get() = null

}
