package com.amary.sisosmed.core.source.remote.network.ktor

import android.content.Context
import com.amary.sisosmed.constant.KeyValue
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object KtorConfig {

    private fun chucker(context: Context): ChuckerInterceptor {
       return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()
    }

    fun build(context: Context): HttpClient {
        return HttpClient(OkHttp){
            install(ContentNegotiation){
                gson()
            }
            engine {
                config {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    addInterceptor(chucker(context))
                    connectTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
                    readTimeout(KeyValue.TIME_NETWORK, TimeUnit.SECONDS)
                    build()
                }

                addInterceptor(chucker(context))
                addNetworkInterceptor(chucker(context))
            }
        }
    }
}