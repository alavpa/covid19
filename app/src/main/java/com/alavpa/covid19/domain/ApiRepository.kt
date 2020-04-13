package com.alavpa.covid19.domain

import com.alavpa.covid19.domain.model.Case
import com.alavpa.covid19.domain.model.Country
import io.reactivex.Single

interface ApiRepository {
    fun getCountries(): Single<List<Country>>
    fun getConfirmed(slug: String):Single<List<Case>>
    fun getDeaths(slug: String):Single<List<Case>>
    fun getRecovered(slug: String):Single<List<Case>>
}