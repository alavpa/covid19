package com.alavpa.covid19.domain.interactors

import com.alavpa.covid19.domain.ApiRepository
import com.alavpa.covid19.domain.interactors.base.Interactor
import com.alavpa.covid19.domain.model.Case
import com.alavpa.covid19.domain.model.Country
import io.reactivex.Single

class GetConfirmed(private val apiRepository: ApiRepository) : Interactor<List<Case>>() {
    var slug: String = ""
    override fun build(): Single<List<Case>> {
        return apiRepository.getConfirmed(slug).map { confirmed ->
            confirmed.groupBy { case -> case.date }.values.map { cases ->
                Case(
                    country = cases[0].country,
                    date = cases[0].date,
                    cases = cases.sumBy { it.cases }
                )
            }.sortedBy { it.date?.time ?: 0L }
        }
    }
}