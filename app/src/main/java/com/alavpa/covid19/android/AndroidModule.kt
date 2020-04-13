package com.alavpa.covid19.android

import android.preference.PreferenceManager
import com.alavpa.covid19.presentation.details.DetailsPresenter
import com.alavpa.covid19.presentation.main.MainPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val androidModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}