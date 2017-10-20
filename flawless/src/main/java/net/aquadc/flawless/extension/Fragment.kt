package net.aquadc.flawless.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.androidView.MvpV4Fragment
import net.aquadc.flawless.parcel.ParcelFunction2

fun Fragment.deliverResult(obj: Parcelable) {
    targetFragment.onActivityResult(
            targetRequestCode, Activity.RESULT_OK, Intent().apply { putExtra("data", obj) }
    )
}

fun Fragment.deliverCancellation() {
    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
}

fun <HOST : MvpV4Fragment<*>, PRESENTER : Presenter<*, *, *, *, *>, RET : Parcelable>
        HOST.willStartForResult(
        newFragment: Fragment, requestCode: Int, callback: ParcelFunction2<PRESENTER, RET, Unit>
) {
    newFragment.setTargetFragment(this, requestCode)
    registerResultCallback(requestCode, callback)
}

// todo: willStartForResult with MvpV4DialogFragment
