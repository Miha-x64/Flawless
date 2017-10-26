package net.aquadc.flawless.solution

import android.os.Parcelable
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import net.aquadc.flawless.parcel.ParcelUnit

interface DataSource<out T : Parcelable> {
    fun subscribe(subscriber: (LoadingResult<T>) -> Unit)
    fun cancel()
}

class SingleAdapter<out T : Parcelable>(private val single: Single<T>) : DataSource<T> {
    private var disposable: Disposable? = null
    override fun subscribe(subscriber: (LoadingResult<T>) -> Unit) {
        if (disposable != null) throw IllegalStateException("Already subscribed")
        disposable = single.subscribe({ subscriber(LoadingResult.Success(it)) }, { subscriber(LoadingResult.Error(it)) })
    }
    override fun cancel() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }
}

class CompletableAdapter(private val single: Completable) : DataSource<ParcelUnit> {
    private var disposable: Disposable? = null
    override fun subscribe(subscriber: (LoadingResult<ParcelUnit>) -> Unit) {
        if (disposable != null) throw IllegalStateException("Already subscribed")
        disposable = single.subscribe({ subscriber(LoadingResult.Success(ParcelUnit)) }, { subscriber(LoadingResult.Error(it)) })
    }
    override fun cancel() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }
}
