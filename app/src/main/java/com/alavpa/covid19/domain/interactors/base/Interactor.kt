package com.alavpa.covid19.domain.interactors.base

import io.reactivex.Single

abstract class Interactor<T> {
    abstract fun build(): Single<T>
}