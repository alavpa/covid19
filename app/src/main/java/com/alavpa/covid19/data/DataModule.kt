package com.alavpa.covid19.data

import com.alavpa.covid19.BuildConfig
import com.alavpa.covid19.domain.ApiRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single { httpLoggingInterceptor() }
    single { okHttpClient(get()) }
    single { getClient(get()) }
    single<ApiRepository> { ApiDataSource(get()) }
}

private fun getClient(okHttpClient: OkHttpClient): Covid19Client {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiDataSource.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Covid19Client::class.java)
}

fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .readTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

fun httpLoggingInterceptor(): HttpLoggingInterceptor = if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
} else {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
}
