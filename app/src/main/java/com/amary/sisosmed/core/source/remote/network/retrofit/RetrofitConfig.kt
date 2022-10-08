package com.amary.sisosmed.core.source.remote.network.retrofit

import android.content.Context
import com.amary.sisosmed.BuildConfig
import com.amary.sisosmed.constant.KeyValue
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    private fun initOkHttp(context: Context): OkHttpClient {
        val chuckedInterceptor =  ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(chuckedInterceptor)
            .connectTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
            .readTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
            .build()
    }

    fun build(context: Context) : RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initOkHttp(context))
            .build()
            .create(RetrofitService::class.java)
    }
}