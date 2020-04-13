package com.alavpa.covid19.data

import com.alavpa.covid19.domain.model.Case
import com.alavpa.covid19.domain.model.Country
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Covid19Client {
    @GET("countries")
    fun countries(): Single<Response<List<Country>>>

    @GET("country/{slug}/status/confirmed")
    fun getConfirmed(@Path("slug") slug: String): Single<Response<List<Case>>>

    @GET("country/{slug}/status/deaths")
    fun getDeaths(@Path("slug") slug: String): Single<Response<List<Case>>>

    @GET("country/{slug}/status/recovered")
    fun getRecovered(@Path("slug") slug: String): Single<Response<List<Case>>>

    @GET("dayone/country/{slug}/status/confirmed")
    fun getDayOneConfirmed(@Path("slug") slug: String): Single<Response<List<Case>>>

    @GET("dayone/country/{slug}/status/deaths")
    fun getDayOneDeaths(@Path("slug") slug: String): Single<Response<List<Case>>>

    @GET("dayone/country/{slug}/status/recovered")
    fun getDayOneRecovered(@Path("slug") slug: String): Single<Response<List<Case>>>
}