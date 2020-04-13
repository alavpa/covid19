package com.alavpa.covid19.presentation.base

import androidx.lifecycle.ViewModel
import com.alavpa.covid19.domain.interactors.base.Interactor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class Presenter : ViewModel() {
    private val disposables = CompositeDisposable()
    fun <T> Interactor<T>.exec(onError: (Throwable) -> Unit = {}, onSuccess: (T) -> Unit) {
        this.build().exec(onError, onSuccess)
    }

    fun <T> Single<T>.exec(onError: (Throwable) -> Unit = {}, onSuccess: (T) -> Unit) {
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
            .also { disposables.add(it) }
    }

    fun destroy() {
        disposables.clear()
    }
}