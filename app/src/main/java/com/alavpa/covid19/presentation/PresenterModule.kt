package com.alavpa.covid19.presentation

import com.alavpa.covid19.presentation.details.DetailsPresenter
import com.alavpa.covid19.presentation.main.MainPresenter
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val presenterModule = module {
    viewModel { MainPresenter(get()) }
    viewModel { DetailsPresenter(get(), get(), get()) }
}