package net.aquadc.flawless.solution

import android.os.Parcelable
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.aquadc.flawless.parcel.ParcelUnit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


/**
 * A future which passes a parcelable result to a callback.
 * Coldness/hotness, multi-subscriptions, subscriptions after completions etc
 * are fully guided by encapsulated actual feature.
 * Generally, you should subscribe only one time.
 */
interface ParcelFuture<out T : Parcelable> {
    fun subscribe(subscriber: (ParcelResult<T>) -> Unit)
    fun cancel()
}


class SingleAdapter<out T : Parcelable>(private val single: Single<T>) : ParcelFuture<T> {
    private var disposable: Disposable? = null
    override fun subscribe(subscriber: (ParcelResult<T>) -> Unit) {
        disposable += single.subscribe({ subscriber(ParcelResult.Success(it)) }, { subscriber(ParcelResult.Error(it)) })
    }
    override fun cancel() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }
}

class CompletableAdapter(private val single: Completable) : ParcelFuture<ParcelUnit> {
    private var disposable: Disposable? = null
    override fun subscribe(subscriber: (ParcelResult<ParcelUnit>) -> Unit) {
        disposable += single.subscribe({ subscriber(ParcelResult.Success(ParcelUnit)) }, { subscriber(ParcelResult.Error(it)) })
    }
    override fun cancel() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }
}

class RetrofitCallAdapter<T : Parcelable>(private val call: Call<T>) : ParcelFuture<T> {
    override fun subscribe(subscriber: (ParcelResult<T>) -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                subscriber(
                        if (response.isSuccessful) ParcelResult.Success(response.body()!!)
                        else ParcelResult.Error(HttpException(response))
                )
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                subscriber(ParcelResult.Error(t))
            }
        })
    }

    override fun cancel() {
        call.cancel()
    }
}

private operator fun Disposable?.plus(other: Disposable): Disposable = when {
    this == null ->
        other

    this !is CompositeDisposable ->
        CompositeDisposable().also { it.add(this); it.add(other) }

    else ->
        this.also { add(other) }
}
