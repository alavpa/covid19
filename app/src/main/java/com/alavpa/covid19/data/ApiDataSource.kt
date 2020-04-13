package com.alavpa.covid19.data

import com.alavpa.covid19.domain.ApiRepository
import com.alavpa.covid19.domain.model.Case
import com.alavpa.covid19.domain.model.Country
import io.reactivex.Single
import retrofit2.Response

class ApiDataSource(private val client: Covid19Client) : ApiRepository {
    companion object {
        const val BASE_URL: String = "https://api.covid19api.com/"
    }

    override fun getCountries(): Single<List<Country>> {
        return client.countries().map { it.process() }
    }

    override fun getConfirmed(slug: String): Single<List<Case>> {
        return client.getDayOneConfirmed(slug).map { it.process() }
    }

    override fun getDeaths(slug: String): Single<List<Case>> {
        return client.getDayOneDeaths(slug).map { it.process() }
    }

    override fun getRecovered(slug: String): Single<List<Case>> {
        return client.getDayOneRecovered(slug).map { it.process() }
    }

    private fun <T> Response<T>.process(): T {
        return if (isSuccessful) body() ?: throw IllegalAccessException("Null body")
        else throw IllegalAccessException(errorBody()?.string() ?: "Empty error")
    }
}