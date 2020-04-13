package com.alavpa.covid19.domain.interactors

import com.alavpa.covid19.domain.ApiRepository
import com.alavpa.covid19.domain.interactors.base.Interactor
import com.alavpa.covid19.domain.model.Country
import io.reactivex.Single

class GetCountries(private val apiRepository: ApiRepository) : Interactor<List<Country>>() {
    override fun build(): Single<List<Country>> {
        return apiRepository.getCountries().map {
            it.sortedBy { country -> country.country }
        }
    }
}