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

interface DataSource<out T : Parcelable> {
    fun subscribe(subscriber: (LoadingResult<T>) -> Unit)
    fun cancel()
}

class SingleAdapter<out T : Parcelable>(private val single: Single<T>) : DataSource<T> {
    private var disposable: Disposable? = null
    override fun subscribe(subscriber: (LoadingResult<T>) -> Unit) {
        disposable += single.subscribe({ subscriber(LoadingResult.Success(it)) }, { subscriber(LoadingResult.Error(it)) })
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
        disposable += single.subscribe({ subscriber(LoadingResult.Success(ParcelUnit)) }, { subscriber(LoadingResult.Error(it)) })
    }
    override fun cancel() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }
}

class RetrofitCallAdapter<T : Parcelable>(private val call: Call<T>) : DataSource<T> {
    override fun subscribe(subscriber: (LoadingResult<T>) -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                subscriber(
                        if (response.isSuccessful) LoadingResult.Success(response.body()!!)
                        else LoadingResult.Error(HttpException(response))
                )
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                subscriber(LoadingResult.Error(t))
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
