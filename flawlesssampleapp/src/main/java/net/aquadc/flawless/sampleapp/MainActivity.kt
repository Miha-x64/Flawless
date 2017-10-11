package net.aquadc.flawless.sampleapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import net.aquadc.flawless.implementMe.Presenter
import net.aquadc.flawless.androidView.MvpFragmentV4
import net.aquadc.flawless.implementMe.PresenterFactory
import net.aquadc.flawless.parcel.ParcelUnit

class MainActivity : AppCompatActivity(), PresenterFactory {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, MvpFragmentV4(RootPresenterTag, ParcelUnit))
                    .commit()
        }
    }

    override fun <ARG : Parcelable, RET : Parcelable, HOST, PARENT, VIEW> createPresenter(
            tag: String
    ): Presenter<ARG, RET, HOST, PARENT, VIEW> =
            when (tag) {
                RootPresenterTag -> RootPresenter(Companion::openDialogFragment, QuestionPresenterTag)
                QuestionPresenterTag -> DialogPresenter()
                else -> throw UnsupportedOperationException()
            } as Presenter<ARG, RET, HOST, PARENT, VIEW>

    private companion object {
        private const val RootPresenterTag = "root"
        private const val QuestionPresenterTag = "question"

        fun openDialogFragment(host: Fragment, new: DialogFragment) {
            new.show(host.fragmentManager, null)
        }
    }

}
