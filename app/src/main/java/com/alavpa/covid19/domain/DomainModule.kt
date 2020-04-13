package com.alavpa.covid19.domain

import com.alavpa.covid19.domain.interactors.GetConfirmed
import com.alavpa.covid19.domain.interactors.GetCountries
import com.alavpa.covid19.domain.interactors.GetDeaths
import com.alavpa.covid19.domain.interactors.GetRecovered
import org.koin.dsl.module.module

val domainModule = module {
    factory { GetCountries(get()) }
    factory { GetConfirmed(get()) }
    factory { GetRecovered(get()) }
    factory { GetDeaths(get()) }
}