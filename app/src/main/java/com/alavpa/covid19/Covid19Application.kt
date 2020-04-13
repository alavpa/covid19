package com.alavpa.covid19

import android.app.Application
import com.alavpa.covid19.android.androidModule
import com.alavpa.covid19.data.dataModule
import com.alavpa.covid19.domain.domainModule
import com.alavpa.covid19.presentation.presenterModule
import org.koin.android.ext.android.startKoin

class Covid19Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(
            androidContext = this,
            modules = listOf(dataModule, domainModule, presenterModule, androidModule)
        )
    }
}